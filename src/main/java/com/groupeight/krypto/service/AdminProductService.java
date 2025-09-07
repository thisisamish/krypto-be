package com.groupeight.krypto.service;

import com.groupeight.krypto.dto.AdminProductCreateRequestDto;
import com.groupeight.krypto.dto.AdminProductUpdateRequestDto;
import com.groupeight.krypto.dto.ProductResponseDto;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminProductService {
    Page<ProductResponseDto> list(Pageable pageable);
    ProductResponseDto get(Long id);
    ProductResponseDto create(AdminProductCreateRequestDto dto);
    ProductResponseDto update(Long id, AdminProductUpdateRequestDto dto);
    void delete(Long id);
}
