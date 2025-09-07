package com.groupeight.krypto.service.impl;

import com.groupeight.krypto.dto.ActiveSessionDto;
import com.groupeight.krypto.model.User;
import com.groupeight.krypto.repository.UserRepository;
import com.groupeight.krypto.service.AdminSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSessionServiceImpl implements AdminSessionService {

    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
    private final UserRepository userRepository;

    @Override
    public List<ActiveSessionDto> byUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return List.of();
        Map<String, ? extends Session> sessions = sessionRepository.findByPrincipalName(user.getUsername());
        return sessions.values().stream().map(this::toDto).toList();
    }

    @Override
    public void invalidate(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    @Override
    public void invalidateAll(Long userId) {
        userRepository.findById(userId).ifPresent(u -> {
            Map<String, ? extends Session> sessions = sessionRepository.findByPrincipalName(u.getUsername());
            sessions.keySet().forEach(sessionRepository::deleteById);
        });
    }

    private ActiveSessionDto toDto(Session s) {
        String username = (String) s.getAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME);
        String role = Optional.ofNullable((String) s.getAttribute("ROLE")).orElse("UNKNOWN");

        Instant created = s.getCreationTime();          // Spring Session provides Instant
        Instant lastAccessed = s.getLastAccessedTime(); // Instant
        Instant expiresAt = lastAccessed.plus(s.getMaxInactiveInterval()); // computed expiry

        return ActiveSessionDto.builder()
                .sessionId(s.getId())
                .username(username)
                .role(role)
                .createdAt(created)
                .lastAccessedAt(lastAccessed)
                .expiresAt(expiresAt)
                .build();
    }
}
