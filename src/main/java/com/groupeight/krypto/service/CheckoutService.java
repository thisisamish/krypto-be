package com.groupeight.krypto.service;

import com.groupeight.krypto.dto.CheckoutRequestDto;
import com.groupeight.krypto.dto.OrderResponseDto;

public interface CheckoutService {
    OrderResponseDto placeOrder(Long userId, CheckoutRequestDto request);
}
