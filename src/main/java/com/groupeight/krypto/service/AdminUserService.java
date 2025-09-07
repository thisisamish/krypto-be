package com.groupeight.krypto.service;

import com.groupeight.krypto.dto.AdminCreateRequestDto;
import com.groupeight.krypto.dto.UserDetailDto;
import com.groupeight.krypto.dto.UserSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminUserService {
    Page<UserSummaryDto> list(String role, String q, Pageable pageable);
    UserDetailDto get(Long userId);
    UserDetailDto createAdmin(AdminCreateRequestDto dto);
    void deleteUser(Long userId);
}
