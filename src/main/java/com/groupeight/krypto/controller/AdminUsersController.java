package com.groupeight.krypto.controller;

import com.groupeight.krypto.dto.*;
import com.groupeight.krypto.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Admin - Users")
@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsersController {

    private final AdminUserService adminUserService;

    @Operation(summary = "List users (filters + sorting + pagination)")
    @GetMapping
    public Page<UserSummaryDto> list(
            @RequestParam(required = false, defaultValue = "ALL") String role,
            @RequestParam(required = false) String q,
            @ParameterObject Pageable pageable
    ) {
        return adminUserService.list(role, q, pageable);
    }

    @Operation(summary = "Get user detail")
    @GetMapping("/{userId}")
    public UserDetailDto get(@PathVariable Long userId) {
        return adminUserService.get(userId);
    }

    @Operation(summary = "Create a new admin user")
    @PostMapping("/admins")
    public UserDetailDto createAdmin(@Valid @RequestBody AdminCreateRequestDto dto) {
        return adminUserService.createAdmin(dto);
    }

    @Operation(summary = "Delete a user or admin (superadmin cannot be deleted)")
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        adminUserService.deleteUser(userId);
    }
}
