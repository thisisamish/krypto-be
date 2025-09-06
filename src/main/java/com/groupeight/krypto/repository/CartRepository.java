package com.groupeight.krypto.repository;

import com.groupeight.krypto.model.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByUserId(Long userId);

	@EntityGraph(attributePaths = "items")
	Optional<Cart> findWithItemsByUserId(Long userId);
}
