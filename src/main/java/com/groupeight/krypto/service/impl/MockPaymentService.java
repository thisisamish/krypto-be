package com.groupeight.krypto.service.impl;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.groupeight.krypto.model.PaymentMethod;
import com.groupeight.krypto.service.PaymentService;

@Service
public class MockPaymentService implements PaymentService {
	@Override
	public PaymentResult charge(PaymentMethod method, BigDecimal amount) {
		if (method == PaymentMethod.COD) {
			return new PaymentResult(true, "COD-" + UUID.randomUUID(), "Cash on delivery");
		}
		// MOCK: fail if amount ends with .13 (handy for tests)
		String s = amount.stripTrailingZeros().toPlainString();
		if (s.endsWith(".13")) {
			return new PaymentResult(false, null, "Gateway rejected amount pattern");
		}
		return new PaymentResult(true, "MOCK-" + UUID.randomUUID(), "Approved");
	}
}
