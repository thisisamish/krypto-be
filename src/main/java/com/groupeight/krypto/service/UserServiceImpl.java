package com.groupeight.krypto.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.groupeight.krypto.dto.UserRegistrationRequestDto;
import com.groupeight.krypto.exception.UserAlreadyExistsException;
import com.groupeight.krypto.model.User;
import com.groupeight.krypto.model.UserRole;
import com.groupeight.krypto.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public void registerUser(UserRegistrationRequestDto dto) throws UserAlreadyExistsException {
		if (userRepository.findByUsername(dto.username()).isPresent()) {
			throw new UserAlreadyExistsException("Username " + dto.username() + " is already taken.");
		}
		
		User newUser = new User();
		newUser.setUsername(dto.username());
		newUser.setPassword(passwordEncoder.encode(dto.password()));
		newUser.setFirstName(dto.firstName());
		newUser.setMiddleName(dto.middleName());
		newUser.setLastName(dto.lastName());
		newUser.setUsername(dto.username());
		newUser.setAddress(dto.address());
		newUser.setUserRole(UserRole.CUSTOMER);
	}
}
