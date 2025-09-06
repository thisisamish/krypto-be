package com.groupeight.krypto.service.impl;

import com.groupeight.krypto.dto.*;
import com.groupeight.krypto.exception.CartEmptyException;
import com.groupeight.krypto.exception.InsufficientStockException;
import com.groupeight.krypto.exception.PaymentFailedException;
import com.groupeight.krypto.model.*;
import com.groupeight.krypto.repository.CartRepository;
import com.groupeight.krypto.repository.OrderRepository;
import com.groupeight.krypto.repository.ProductRepository;
import com.groupeight.krypto.repository.UserRepository;
import com.groupeight.krypto.service.CheckoutService;
import com.groupeight.krypto.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;

    private static final AtomicInteger DAILY_COUNTER = new AtomicInteger(0);
    private static String currentDatePrefix() {
        return DateTimeFormatter.ofPattern("yyyyMMdd").format(java.time.LocalDate.now());
    }

    @Override
    @Transactional
    public OrderResponseDto placeOrder(Long userId, CheckoutRequestDto request) {
        Cart cart = cartRepository.findWithItemsByUserId(userId)
                .orElseThrow(() -> new CartEmptyException("Cart not found"));
        if (cart.getItems().isEmpty()) throw new CartEmptyException("Cart is empty");

        // Validate & reserve stock (optimistic lock on products)
        cart.getItems().forEach(ci -> {
            Product p = productRepository.findWithLockById(ci.getProduct().getId())
                    .orElseThrow(() -> new InsufficientStockException("Product not found: " + ci.getProduct().getId()));
            if (p.getStockQuantity() < ci.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + p.getName());
            }
            p.setStockQuantity(p.getStockQuantity() - ci.getQuantity());
            // save via flush later
        });

        // Compute totals
        BigDecimal subtotal = cart.getItems().stream()
                .map(ci -> ci.getUnitPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal shipping = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal grand = subtotal.add(tax).add(shipping).subtract(discount);

        // Create order
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUser(userRepository.findById(userId).orElseThrow());
        order.setStatus(OrderStatus.CREATED);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setSubtotal(subtotal);
        order.setTax(tax);
        order.setShippingFee(shipping);
        order.setDiscount(discount);
        order.setGrandTotal(grand);

        Address addr = new Address();
        AddressDto ad = request.getShippingAddress();
        addr.setFullName(ad.getFullName());
        addr.setLine1(ad.getLine1());
        addr.setLine2(ad.getLine2());
        addr.setCity(ad.getCity());
        addr.setState(ad.getState());
        addr.setPostalCode(ad.getPostalCode());
        addr.setCountry(ad.getCountry());
        addr.setPhone(ad.getPhone());
        order.setShippingAddress(addr);
        order.setNotes(request.getNotes());

        cart.getItems().forEach(ci -> {
            OrderItem oi = OrderItem.builder()
                    .productId(ci.getProduct().getId())
                    .productName(ci.getProduct().getName())
                    .unitPrice(ci.getUnitPrice())
                    .quantity(ci.getQuantity())
                    .lineTotal(ci.getUnitPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                    .build();
            order.addItem(oi);
        });

        // Attempt payment
        PaymentService.PaymentResult result = paymentService.charge(request.getPaymentMethod(), grand);
        if (!result.success()) {
            // rollback TX automatically
            throw new PaymentFailedException("Payment failed: " + result.message());
        }
        order.setPaymentStatus(PaymentStatus.SUCCESS);
        order.setStatus(OrderStatus.PAID);
        order.setPaymentReference(result.reference());
        order.setPaidAt(Instant.now());

        // Persist
        orderRepository.save(order);

        // Persist product stock changes (implicit by JPA dirty tracking)
        // Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        return OrderMapper.toResponseDto(order);
    }

    private String generateOrderNumber() {
        String date = currentDatePrefix();
        int seq = DAILY_COUNTER.updateAndGet(prev -> {
            String today = currentDatePrefix();
            // reset if date changed
            if (!date.equals(today)) return 1;
            return prev + 1;
        });
        return String.format("KSF-%s-%04d", date, seq);
    }

    /** Simple mapper (kept local to avoid adding MapStruct) */
    static class OrderMapper {
        static OrderResponseDto toResponseDto(Order o) {
            return OrderResponseDto.builder()
                    .orderNumber(o.getOrderNumber())
                    .status(o.getStatus())
                    .paymentStatus(o.getPaymentStatus())
                    .paymentMethod(o.getPaymentMethod())
                    .subtotal(o.getSubtotal())
                    .tax(o.getTax())
                    .shippingFee(o.getShippingFee())
                    .discount(o.getDiscount())
                    .grandTotal(o.getGrandTotal())
                    .createdAt(o.getCreatedAt())
                    .paidAt(o.getPaidAt())
                    .shippingAddress(toAddressDto(o.getShippingAddress()))
                    .notes(o.getNotes())
                    .items(o.getItems().stream().map(i -> OrderItemDto.builder()
                            .productId(i.getProductId())
                            .productName(i.getProductName())
                            .unitPrice(i.getUnitPrice())
                            .quantity(i.getQuantity())
                            .lineTotal(i.getLineTotal())
                            .build()).toList())
                    .build();
        }

        static AddressDto toAddressDto(Address a) {
            if (a == null) return null;
            AddressDto dto = new AddressDto();
            dto.setFullName(a.getFullName());
            dto.setLine1(a.getLine1());
            dto.setLine2(a.getLine2());
            dto.setCity(a.getCity());
            dto.setState(a.getState());
            dto.setPostalCode(a.getPostalCode());
            dto.setCountry(a.getCountry());
            dto.setPhone(a.getPhone());
            return dto;
        }
    }
}
