package com.groupeight.krypto.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groupeight.krypto.dto.ProductRequestDto;
import com.groupeight.krypto.dto.ProductResponseDto;
import com.groupeight.krypto.exception.ResourceNotFoundException;
import com.groupeight.krypto.model.Product;
import com.groupeight.krypto.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
	public static Product toEntity(ProductRequestDto dto) {
		Product product = new Product();
		product.setName(dto.name());
		product.setDescription(dto.description());
		product.setPrice(dto.price());
		product.setStockQuantity(dto.stockQuantity());
		product.setImageUrl(dto.imageUrl());

		return product;
	}

	private final ProductRepository productRepository;

	@Override
	public ProductResponseDto createProduct(ProductRequestDto dto) {
		Product productToSave = toEntity(dto);
		Product savedProduct = productRepository.save(productToSave);

		return ProductResponseDto.fromEntity(savedProduct);
	}

	@Override
	public void deleteProduct(Long id) {
		if (!productRepository.existsById(id)) {
			throw new ResourceNotFoundException("Product with ID: " + id + " not found.");
		}
		productRepository.deleteById(id);
	}

	@Override
	public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
		Page<Product> allProducts = productRepository.findAll(pageable);

		return allProducts.map(ProductResponseDto::fromEntity);
	}

	@Override
	public ProductResponseDto getProductById(Long id) {
		return ProductResponseDto.fromEntity(productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found.")));
	}

	@Override
	public ProductResponseDto updateProduct(Long id, ProductRequestDto dto) {
		Product existingProduct = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product with ID: " + id + " not found."));

		existingProduct.setName(dto.name());
		existingProduct.setDescription(dto.description());
		existingProduct.setPrice(dto.price());
		existingProduct.setStockQuantity(dto.stockQuantity());
		existingProduct.setImageUrl(dto.imageUrl());

		Product updatedProduct = productRepository.save(existingProduct);

		return ProductResponseDto.fromEntity(updatedProduct);
	}
}