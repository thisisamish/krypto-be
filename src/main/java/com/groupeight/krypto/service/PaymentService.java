package com.groupeight.krypto.service;

import com.groupeight.krypto.model.PaymentMethod;

import java.math.BigDecimal;

public interface PaymentService {
    PaymentResult charge(PaymentMethod method, BigDecimal amount);

    record PaymentResult(boolean success, String reference, String message) {}
}
