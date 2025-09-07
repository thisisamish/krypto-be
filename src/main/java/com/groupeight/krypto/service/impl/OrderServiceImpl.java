package com.groupeight.krypto.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.groupeight.krypto.dto.OrderResponseDto;
import com.groupeight.krypto.dto.OrderSummaryDto;
import com.groupeight.krypto.exception.ResourceNotFoundException;
import com.groupeight.krypto.model.Order;
import com.groupeight.krypto.repository.OrderRepository;
import com.groupeight.krypto.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	@Override
	public OrderResponseDto getMyOrder(String orderNumber, Long userId) {
		Order o = orderRepository.findWithItemsByOrderNumberAndUserId(orderNumber, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderNumber));
		return CheckoutServiceImpl.OrderMapper.toResponseDto(o);
	}

	@Override
	public Page<OrderSummaryDto> listMyOrders(Long userId, Pageable pageable) {
		return null;
//		return orderRepository.findByUserId(userId, pageable)
//				.map(o -> new OrderSummaryDto(o.getOrderNumber(), o.getStatus(), o.getGrandTotal(), o.getCreatedAt()));
	}
}
