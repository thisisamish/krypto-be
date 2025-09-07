package com.groupeight.krypto.service.impl;

import com.groupeight.krypto.dto.OrderDetailDto;
import com.groupeight.krypto.dto.OrderDetailDto.AddressDto;
import com.groupeight.krypto.dto.OrderDetailDto.OrderItemDto;
import com.groupeight.krypto.dto.OrderSummaryDto;
import com.groupeight.krypto.exception.ResourceNotFoundException;
import com.groupeight.krypto.model.*;
import com.groupeight.krypto.repository.OrderRepository;
import com.groupeight.krypto.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryDto> list(String orderNumber, String status, String paymentStatus, String userId,
                                      String dateFrom, String dateTo, Pageable pageable) {
        Page<Order> page = orderRepository.findAll(pageable);
        // MVP in-memory filters; real DB would use Specifications
        return page.map(this::toSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailDto get(String orderNumber) {
        Order o = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return toDetail(o);
    }

    private OrderSummaryDto toSummary(Order o) {
        Long uid = o.getUser() != null ? o.getUser().getId() : null;
        return OrderSummaryDto.builder()
                .orderNumber(o.getOrderNumber())
                .userId(uid)
                .usernameSnapshot(o.getUsernameSnapshot())
                .status(o.getStatus())
                .paymentStatus(o.getPaymentStatus())
                .grandTotal(o.getGrandTotal())
                .createdAt(o.getCreatedAt())
                .build();
    }

    private OrderDetailDto toDetail(Order o) {
        Address addr = o.getShippingAddress();
        AddressDto addrDto = addr == null ? null : AddressDto.builder()
                .line1(addr.getLine1()).line2(addr.getLine2())
                .city(addr.getCity()).state(addr.getState())
                .postalCode(addr.getPostalCode()).country(addr.getCountry())
                .build();

        OrderDetailDto.OrderDetailDtoBuilder b = OrderDetailDto.builder()
                .orderNumber(o.getOrderNumber())
                .userId(o.getUser() != null ? o.getUser().getId() : null)
                .usernameSnapshot(o.getUsernameSnapshot())
                .status(o.getStatus().name())
                .paymentStatus(o.getPaymentStatus().name())
                .paymentMethod(o.getPaymentMethod() != null ? o.getPaymentMethod().name() : null)
                .paymentReference(o.getPaymentReference())
                .subtotal(o.getSubtotal())
                .tax(o.getTax())
                .shippingFee(o.getShippingFee())
                .discount(o.getDiscount())
                .grandTotal(o.getGrandTotal())
                .createdAt(o.getCreatedAt())
                .paidAt(o.getPaidAt())
                .shippingAddress(addrDto)
                .notes(o.getNotes());

        for (OrderItem item : o.getItems()) {
            b.item(OrderItemDto.builder()
                    .productId(item.getProductId())
                    .productName(item.getProductName())
                    .unitPrice(item.getUnitPrice())
                    .quantity(item.getQuantity())
                    .lineTotal(item.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())))
                    .build());
        }
        return b.build();
    }
}
