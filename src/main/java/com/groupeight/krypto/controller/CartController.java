package com.groupeight.krypto.controller;

import com.groupeight.krypto.dto.*;
import com.groupeight.krypto.model.User;
import com.groupeight.krypto.repository.UserRepository;
import com.groupeight.krypto.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    @GetMapping("/")
    public CartResponseDto getCart(Authentication auth) {
        Long userId = getUserId(auth);
        return cartService.getMyCart(userId);
    }

    @PostMapping("/items")
    public CartResponseDto addItem(Authentication auth, @Valid @RequestBody CartItemRequestDto req) {
        Long userId = getUserId(auth);
        return cartService.addItem(userId, req.getProductId(), req.getQuantity());
    }

    @PutMapping("/items/{productId}")
    public CartResponseDto updateItem(Authentication auth,
                                      @PathVariable Long productId,
                                      @Valid @RequestBody CartItemUpdateRequestDto req) {
        Long userId = getUserId(auth);
        return cartService.updateItem(userId, productId, req.getQuantity());
    }

    @DeleteMapping("/items/{productId}")
    public void removeItem(Authentication auth, @PathVariable Long productId) {
        Long userId = getUserId(auth);
        cartService.removeItem(userId, productId);
    }

    @DeleteMapping("/")
    public void clear(Authentication auth) {
        cartService.clearCart(getUserId(auth));
    }

    private Long getUserId(Authentication auth) {
        String username = auth.getName();
        User u = userRepository.findByUsername(username).orElseThrow();
        return u.getId();
    }
}
