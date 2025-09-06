package com.groupeight.krypto.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groupeight.krypto.dto.CheckoutRequestDto;
import com.groupeight.krypto.dto.OrderResponseDto;
import com.groupeight.krypto.repository.UserRepository;
import com.groupeight.krypto.service.CheckoutService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
public class CheckoutController {

	private final CheckoutService checkoutService;
	private final UserRepository userRepository;

	@PostMapping("/place-order")
	public OrderResponseDto placeOrder(Authentication auth, @Valid @RequestBody CheckoutRequestDto req) {
		Long userId = userRepository.findByUsername(auth.getName()).orElseThrow().getId();
		return checkoutService.placeOrder(userId, req);
	}
}
