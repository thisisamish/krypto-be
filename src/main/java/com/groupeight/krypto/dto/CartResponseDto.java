package com.groupeight.krypto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CartResponseDto {
    private final List<CartItemResponseDto> items;
    private final BigDecimal subtotal;
    private final int itemCount;
}
