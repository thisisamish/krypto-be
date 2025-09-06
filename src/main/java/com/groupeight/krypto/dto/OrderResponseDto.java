package com.groupeight.krypto.dto;

import com.groupeight.krypto.model.OrderStatus;
import com.groupeight.krypto.model.PaymentMethod;
import com.groupeight.krypto.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private final String orderNumber;
    private final OrderStatus status;
    private final PaymentStatus paymentStatus;
    private final PaymentMethod paymentMethod;
    private final BigDecimal subtotal;
    private final BigDecimal tax;
    private final BigDecimal shippingFee;
    private final BigDecimal discount;
    private final BigDecimal grandTotal;
    private final Instant createdAt;
    private final Instant paidAt;
    private final AddressDto shippingAddress;
    private final String notes;
    private final List<OrderItemDto> items;
}
