package com.groupeight.krypto.repository;

import com.groupeight.krypto.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserId(Long userId, Pageable pageable);

    Optional<Order> findByOrderNumberAndUserId(String orderNumber, Long userId);

    @EntityGraph(attributePaths = "items")
    Optional<Order> findWithItemsByOrderNumberAndUserId(String orderNumber, Long userId);

    Optional<Order> findByOrderNumber(String orderNumber);
}
