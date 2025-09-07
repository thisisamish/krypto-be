package com.groupeight.krypto.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

import com.groupeight.krypto.model.UserRole;

@Value
@Builder
public class UserDetailDto {
    Long id;
    String username;
    UserRole role;
    String email;
    String firstName;
    String lastName;
    Instant createdAt;
}
