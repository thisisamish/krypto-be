package com.groupeight.krypto.dto;

import java.math.BigDecimal;

import com.groupeight.krypto.model.Product;

public record ProductResponseDto(Long id, String name, String description, BigDecimal price, int stockQuantity,
		String imageUrl) {
	public static ProductResponseDto fromEntity(Product product) {
		return new ProductResponseDto(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
				product.getStockQuantity(), product.getImageUrl());
	}
}