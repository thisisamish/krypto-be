
package com.groupeight.krypto.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for CRUD on products. The 'unlocked lock' icon in front of the endpoints indicates that they require authorization.")
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	private final ProductService productService;

	@GetMapping("/")
	@PageableAsQueryParam
	@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
	@Operation(summary = "Get a paginated and sorted list of all products. Requires ADMIN or CUSTOMER role.")
	@SecurityRequirement(name = "cookieAuth")
	public ResponseEntity<Page<ProductResponseDto>> getAllProducts(@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(productService.getAllProducts(pageable));
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
	@Operation(summary = "Get a single product by its ID. Requires ADMIN or CUSTOMER role.")
	@SecurityRequirement(name = "cookieAuth")
	public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}
}


