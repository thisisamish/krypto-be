package com.groupeight.krypto.dto;

import com.groupeight.krypto.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@AllArgsConstructor
@Builder
public class OrderSummaryDto {
    private final String orderNumber;
    private final OrderStatus status;
    private final BigDecimal grandTotal;
    private final Instant createdAt;
}
