package com.groupeight.krypto.service.impl;

import com.groupeight.krypto.dto.*;
import com.groupeight.krypto.exception.InvalidCartOperationException;
import com.groupeight.krypto.exception.ResourceNotFoundException;
import com.groupeight.krypto.model.Cart;
import com.groupeight.krypto.model.CartItem;
import com.groupeight.krypto.model.Product;
import com.groupeight.krypto.repository.*;
import com.groupeight.krypto.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public CartResponseDto getMyCart(Long userId) {
        Cart cart = cartRepository.findWithItemsByUserId(userId)
                .orElseGet(() -> createEmptyCart(userId));
        return toDto(cart);
    }

    @Override
    @Transactional
    public CartResponseDto addItem(Long userId, Long productId, int quantity) {
        if (quantity < 1) throw new InvalidCartOperationException("Quantity must be >= 1");
        Cart cart = cartRepository.findWithItemsByUserId(userId).orElseGet(() -> initCart(userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst().orElse(null);

        int newQty = quantity + (item == null ? 0 : item.getQuantity());
        if (product.getStockQuantity() < newQty) {
            throw new InvalidCartOperationException("Requested quantity exceeds available stock");
        }

        if (item == null) {
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setUnitPrice(product.getPrice()); // snapshot
            cart.getItems().add(item);
        } else {
            item.setQuantity(newQty);
            item.setUnitPrice(product.getPrice()); // refresh to current price on change
        }

        cartRepository.save(cart);
        return toDto(cart);
    }

    @Override
    @Transactional
    public CartResponseDto updateItem(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findWithItemsByUserId(userId)
                .orElseThrow(() -> new InvalidCartOperationException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new InvalidCartOperationException("Item not in cart"));

        if (quantity == 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            Product product = item.getProduct();
            if (product.getStockQuantity() < quantity) {
                throw new InvalidCartOperationException("Requested quantity exceeds available stock");
            }
            item.setQuantity(quantity);
            item.setUnitPrice(product.getPrice());
        }
        cartRepository.save(cart);
        return toDto(cart);
    }

    @Override
    @Transactional
    public CartResponseDto removeItem(Long userId, Long productId) {
        Cart cart = cartRepository.findWithItemsByUserId(userId)
                .orElseThrow(() -> new InvalidCartOperationException("Cart not found"));
        cart.getItems().removeIf(ci -> ci.getProduct().getId().equals(productId));
        return toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartRepository.findWithItemsByUserId(userId).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepository.save(cart);
        });
    }

    private Cart initCart(Long userId) {
        Cart cart = new Cart();
        cart.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId)));
        return cartRepository.save(cart);
    }

    private Cart createEmptyCart(Long userId) {
        Cart cart = new Cart();
        cart.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId)));
        cart.setItems(List.of());
        return cart;
    }

    private CartResponseDto toDto(Cart cart) {
        List<CartItemResponseDto> items = cart.getItems().stream()
                .sorted(Comparator.comparing(ci -> ci.getProduct().getName()))
                .map(ci -> CartItemResponseDto.builder()
                        .productId(ci.getProduct().getId())
                        .productName(ci.getProduct().getName())
                        .unitPrice(ci.getUnitPrice())
                        .quantity(ci.getQuantity())
                        .lineTotal(ci.getUnitPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                        .build())
                .toList();

        BigDecimal subtotal = items.stream()
                .map(CartItemResponseDto::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int count = cart.getItems().stream().mapToInt(CartItem::getQuantity).sum();

        return CartResponseDto.builder()
                .items(items)
                .subtotal(subtotal)
                .itemCount(count)
                .build();
    }
}
