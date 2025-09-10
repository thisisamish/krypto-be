package com.groupeight.krypto.service;

import com.groupeight.krypto.dto.UserProfileResponseDto;
import com.groupeight.krypto.dto.UserProfileUpdateRequestDto;
import com.groupeight.krypto.dto.UserRegistrationRequestDto;

public interface UserService {
	public void registerUser(UserRegistrationRequestDto dto);
	
	UserProfileResponseDto getProfile(String username);
	UserProfileResponseDto updateProfile(String username, UserProfileUpdateRequestDto dto);
}
