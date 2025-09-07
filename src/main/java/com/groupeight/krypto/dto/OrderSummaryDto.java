package com.groupeight.krypto.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.groupeight.krypto.model.OrderStatus;
import com.groupeight.krypto.model.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class OrderSummaryDto {
	private final Long userId;
	private final String orderNumber;
	private final OrderStatus status;
	private final BigDecimal grandTotal;
	private final Instant createdAt;
	private final String usernameSnapshot;
	private final PaymentStatus paymentStatus;
}
