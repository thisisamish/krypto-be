package com.groupeight.krypto.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class ActiveSessionDto {
    String sessionId;
    String username;
    String role;
    Instant createdAt;
    Instant lastAccessedAt;
    Instant expiresAt;
}
