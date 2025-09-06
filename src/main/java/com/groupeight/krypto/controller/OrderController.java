package com.groupeight.krypto.controller;

import com.groupeight.krypto.dto.OrderResponseDto;
import com.groupeight.krypto.dto.OrderSummaryDto;
import com.groupeight.krypto.repository.UserRepository;
import com.groupeight.krypto.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    @GetMapping
    public Page<OrderSummaryDto> list(Authentication auth, Pageable pageable) {
        Long userId = userRepository.findByUsername(auth.getName()).orElseThrow().getId();
        return orderService.listMyOrders(userId, pageable);
    }

    @GetMapping("/{orderNumber}")
    public OrderResponseDto get(Authentication auth, @PathVariable String orderNumber) {
        Long userId = userRepository.findByUsername(auth.getName()).orElseThrow().getId();
        return orderService.getMyOrder(orderNumber, userId);
    }
}
