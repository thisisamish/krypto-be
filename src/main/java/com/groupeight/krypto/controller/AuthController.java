package com.groupeight.krypto.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groupeight.krypto.dto.CurrentUserDto;
import com.groupeight.krypto.dto.UserRegistrationRequestDto;
import com.groupeight.krypto.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user login and logout")
public class AuthController {

	private final UserService userService;

	@Operation(summary = "Get Current User", description = "Checks the current session and returns the logged-in user's details.", responses = {
			@ApiResponse(description = "User is authenticated", responseCode = "200"),
			@ApiResponse(description = "User is not authenticated", responseCode = "401", content = @Content) })
	@GetMapping("/me")
	public ResponseEntity<CurrentUserDto> getCurrentUser(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String username = authentication.getName();
		String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		CurrentUserDto currentUser = new CurrentUserDto(username, role);
		return ResponseEntity.ok(currentUser);
	}

	@Operation(summary = "User Login", description = "Authenticates a user and establishes a session. Use this endpoint first to get a session cookie.", responses = {
			@ApiResponse(description = "Login successful", responseCode = "200"),
			@ApiResponse(description = "Authentication failed", responseCode = "401", content = @Content) })
	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void login(@Parameter(description = "Users's username", required = true) @RequestParam String username,
			@Parameter(description = "User's password", required = true) @RequestParam String password) {
		throw new IllegalStateException(
				"This method should not be called. It's for Swagger documentation purposes only.");
	}

	@Operation(summary = "User Logout", description = "Logs out a user", responses = {
			@ApiResponse(description = "Logout successful", responseCode = "200") })
	@PostMapping(value = "/logout")
	public void logout() {
		throw new IllegalStateException(
				"This method should not be called. It's for Swagger documentation purposes only.");
	}

	@Operation(summary = "Register User", description = "Registers a user (CUSTOMER role only).")
	@PostMapping(value = "/register")
	public ResponseEntity<Void> register(@Valid @RequestBody UserRegistrationRequestDto dto) {
		userService.registerUser(dto);
		return ResponseEntity.noContent().build();
	}
}