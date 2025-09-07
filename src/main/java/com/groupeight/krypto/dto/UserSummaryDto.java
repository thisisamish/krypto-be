package com.groupeight.krypto.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

import com.groupeight.krypto.model.UserRole;

@Value
@Builder
public class UserSummaryDto {
    Long id;
    String username;
    UserRole role;
    String email;
    Instant createdAt;
}
