package com.groupeight.krypto.service;

import java.math.BigDecimal;

import com.groupeight.krypto.model.PaymentMethod;

public interface PaymentService {
	record PaymentResult(boolean success, String reference, String message) {
	}

	PaymentResult charge(PaymentMethod method, BigDecimal amount);
}
