package com.groupeight.krypto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groupeight.krypto.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByUserId(Long userId);
}