package com.groupeight.krypto.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.groupeight.krypto.dto.UserRegistrationRequestDto;
import com.groupeight.krypto.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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