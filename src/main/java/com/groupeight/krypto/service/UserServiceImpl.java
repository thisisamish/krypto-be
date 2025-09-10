package com.groupeight.krypto.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.groupeight.krypto.dto.UserProfileResponseDto;
import com.groupeight.krypto.dto.UserProfileUpdateRequestDto;
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

	@Override
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
		newUser.setEmail(dto.email());
		newUser.setIsSuperAdmin(false);
		newUser.setUserRole(UserRole.CUSTOMER);
		
		userRepository.save(newUser);
	}
	
	@Override
	public UserProfileResponseDto getProfile(String username) {
	    User user = userRepository.findByUsername(username)
	        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

	    return new UserProfileResponseDto(
	        user.getUsername(),
	        user.getFirstName(),
	        user.getMiddleName(),
	        user.getLastName(),
	        user.getEmail(),
	        user.getAddress(),
	        user.getContactNo()
	    );
	}

	@Override
	public UserProfileResponseDto updateProfile(String username, UserProfileUpdateRequestDto dto) {
	    User user = userRepository.findByUsername(username)
	        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

	    user.setFirstName(dto.firstName());
	    user.setMiddleName(dto.middleName());
	    user.setLastName(dto.lastName());
	    user.setEmail(dto.email());
	    user.setAddress(dto.address());
	    user.setContactNo(dto.contactNo());

	    userRepository.save(user);

	    return new UserProfileResponseDto(
	        user.getUsername(),
	        user.getFirstName(),
	        user.getMiddleName(),
	        user.getLastName(),
	        user.getEmail(),
	        user.getAddress(),
	        user.getContactNo()
	    );
	}

}
