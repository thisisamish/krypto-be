package com.groupeight.krypto.dto;

import com.groupeight.krypto.model.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CheckoutRequestDto {
    @NotNull private PaymentMethod paymentMethod;
    @Valid @NotNull private AddressDto shippingAddress;
    @Size(max = 512) private String notes;
}
