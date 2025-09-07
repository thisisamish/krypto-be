package com.groupeight.krypto.service;

import com.groupeight.krypto.dto.OrderDetailDto;
import com.groupeight.krypto.dto.OrderSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminOrderService {
    Page<OrderSummaryDto> list(String orderNumber, String status, String paymentStatus, String userId,
                               String dateFrom, String dateTo, Pageable pageable);
    OrderDetailDto get(String orderNumber);
}
