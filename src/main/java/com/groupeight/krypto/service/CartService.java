package com.groupeight.krypto.service;

import com.groupeight.krypto.dto.*;

public interface CartService {
    CartResponseDto getMyCart(Long userId);
    CartResponseDto addItem(Long userId, Long productId, int quantity);
    CartResponseDto updateItem(Long userId, Long productId, int quantity);
    CartResponseDto removeItem(Long userId, Long productId);
    void clearCart(Long userId);
}
