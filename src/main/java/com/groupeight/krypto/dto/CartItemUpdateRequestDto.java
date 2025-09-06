package com.groupeight.krypto.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartItemUpdateRequestDto {
    /** 0 removes the item */
    @Min(0)
    private int quantity;
}
