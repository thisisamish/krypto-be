package com.groupeight.krypto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groupeight.krypto.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
