package com.groupeight.krypto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupeight.krypto.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}