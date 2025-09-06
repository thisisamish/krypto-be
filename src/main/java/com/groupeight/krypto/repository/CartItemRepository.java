package com.groupeight.krypto.repository;

import com.groupeight.krypto.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

	void deleteByCartIdAndProductId(Long cartId, Long productId);
}
