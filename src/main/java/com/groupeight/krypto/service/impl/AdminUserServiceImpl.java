package com.groupeight.krypto.service.impl;

import com.groupeight.krypto.dto.AdminCreateRequestDto;
import com.groupeight.krypto.dto.UserDetailDto;
import com.groupeight.krypto.dto.UserSummaryDto;
import com.groupeight.krypto.exception.ResourceNotFoundException;
import com.groupeight.krypto.model.Order;
import com.groupeight.krypto.model.User;
import com.groupeight.krypto.model.UserRole;
import com.groupeight.krypto.repository.OrderRepository;
import com.groupeight.krypto.repository.UserRepository;
import com.groupeight.krypto.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<UserSummaryDto> list(String role, String q, Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return page.map(u -> toSummary(u))
                .map(d -> d); // passthrough
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailDto get(Long userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toDetail(u);
    }

    @Override
    public UserDetailDto createAdmin(@Valid AdminCreateRequestDto dto) {
        if (userRepository.existsByUsernameIgnoreCase(dto.getUsername())) {
            throw new DataIntegrityViolationException("Username already exists");
        }
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        u.setEmail(dto.getEmail());
        u.setUserRole(UserRole.ADMIN);
        userRepository.save(u);
        return toDetail(u);
    }

    @Override
    public void deleteUser(Long userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (Boolean.TRUE.equals(u.getIsSuperAdmin())) {
            throw new IllegalStateException("Superadmin cannot be deleted");
        }

        userRepository.delete(u);
    }

    private UserSummaryDto toSummary(User u) {
        if (!StringUtils.hasText(u.getUserRole().name())) {
            // Should never happen, but keep DTO defensive
        }
        return UserSummaryDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .role(u.getUserRole())
                .email(u.getEmail())
                .createdAt(u.getCreatedAt())
                .build();
    }

    private UserDetailDto toDetail(User u) {
        return UserDetailDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .role(u.getUserRole())
                .email(u.getEmail())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .createdAt(u.getCreatedAt())
                .build();
    }
}
