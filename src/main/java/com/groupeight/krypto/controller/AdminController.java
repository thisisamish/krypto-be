package com.groupeight.krypto.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groupeight.krypto.dto.ProductRequestDto;
import com.groupeight.krypto.dto.ProductResponseDto;
import com.groupeight.krypto.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

	private final ProductService productService;

	@PostMapping("/")
	public ResponseEntity<?> addAdmin() {
//		TODO: DO this
		return null;
	}

	@PostMapping("/products")
	@Operation(summary = "Create a new product. Requires ADMIN role.")
	@SecurityRequirement(name = "cookieAuth")
	public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto dto) {
		ProductResponseDto createdProduct = productService.createProduct(dto);

		return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
	}

	@DeleteMapping("/")
	public ResponseEntity<Void> deleteAdmin() {
//		TODO: Do this
		return null;
	}

	@DeleteMapping("/products/{id}")
	@Operation(summary = "Delete a product. Requires ADMIN role.")
	@SecurityRequirement(name = "cookieAuth")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {
//		TODO: Can get all users or all logged in users
		return null;
	}

	@PutMapping("/")
	public ResponseEntity<?> updateAdmin() {
//		TODO: Do this
		return null;
	}

	@PutMapping("/products/{id}")
	@Operation(summary = "Update an existing product. Requires ADMIN role.")
	@SecurityRequirement(name = "cookieAuth")
	public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id,
			@Valid @RequestBody ProductRequestDto dto) {
		ProductResponseDto updatedProduct = productService.updateProduct(id, dto);

		return ResponseEntity.ok(updatedProduct);
	}
}
