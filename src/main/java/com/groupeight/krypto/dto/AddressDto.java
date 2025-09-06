package com.groupeight.krypto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
	@NotBlank
	private String fullName;
	@NotBlank
	private String line1;
	@Size(max = 255)
	private String line2;
	@NotBlank
	private String city;
	@NotBlank
	private String state;
	@NotBlank
	private String postalCode;
	@NotBlank
	private String country;
	@NotBlank
	private String phone;
}
