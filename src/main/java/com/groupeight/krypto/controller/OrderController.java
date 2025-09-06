package com.groupeight.krypto.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groupeight.krypto.dto.OrderResponseDto;
import com.groupeight.krypto.dto.OrderSummaryDto;
import com.groupeight.krypto.repository.UserRepository;
import com.groupeight.krypto.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
public class OrderController {

	private final OrderService orderService;
	private final UserRepository userRepository;

	@GetMapping("/{orderNumber}")
	public OrderResponseDto get(Authentication auth, @PathVariable String orderNumber) {
		Long userId = userRepository.findByUsername(auth.getName()).orElseThrow().getId();
		return orderService.getMyOrder(orderNumber, userId);
	}

	@GetMapping
	public Page<OrderSummaryDto> list(Authentication auth, Pageable pageable) {
		Long userId = userRepository.findByUsername(auth.getName()).orElseThrow().getId();
		return orderService.listMyOrders(userId, pageable);
	}
}
