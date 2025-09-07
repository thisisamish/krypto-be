package com.groupeight.krypto.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminCreateRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String email;
}
