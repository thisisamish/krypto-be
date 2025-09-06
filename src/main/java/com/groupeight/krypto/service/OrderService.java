package com.groupeight.krypto.service;

import com.groupeight.krypto.dto.OrderResponseDto;
import com.groupeight.krypto.dto.OrderSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderSummaryDto> listMyOrders(Long userId, Pageable pageable);
    OrderResponseDto getMyOrder(String orderNumber, Long userId);
}
