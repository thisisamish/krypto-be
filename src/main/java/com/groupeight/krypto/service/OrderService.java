package com.groupeight.krypto.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.groupeight.krypto.dto.OrderResponseDto;
import com.groupeight.krypto.dto.OrderSummaryDto;

public interface OrderService {
	OrderResponseDto getMyOrder(String orderNumber, Long userId);

	Page<OrderSummaryDto> listMyOrders(Long userId, Pageable pageable);
}
