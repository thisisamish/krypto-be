package com.groupeight.krypto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.groupeight.krypto.model.Product;

import jakarta.persistence.LockModeType;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findByName(String name);

	@Lock(LockModeType.OPTIMISTIC)
	Optional<Product> findWithLockById(Long id);
}
