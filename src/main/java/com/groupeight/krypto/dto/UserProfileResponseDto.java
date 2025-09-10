package com.groupeight.krypto.dto;

public record UserProfileResponseDto(
	    String username,
	    String firstName,
	    String middleName,
	    String lastName,
	    String email,
	    String address,
	    String contactNo
	) {}
