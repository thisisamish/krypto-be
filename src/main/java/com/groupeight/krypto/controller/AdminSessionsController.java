package com.groupeight.krypto.controller;

import com.groupeight.krypto.dto.ActiveSessionDto;
import com.groupeight.krypto.service.AdminSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Admin - Sessions")
@RestController
@RequestMapping("/api/v1/admin/sessions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSessionsController {

    private final AdminSessionService adminSessionService;

    @Operation(summary = "List sessions for a specific user")
    @GetMapping("/{userId}")
    public ResponseEntity<List<ActiveSessionDto>> byUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminSessionService.byUser(userId));
    }

    @Operation(summary = "Invalidate one session of a user (force logout)")
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> invalidate(@PathVariable String sessionId) {
        adminSessionService.invalidate(sessionId);
        
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Invalidate all sessions of a user (force logout everywhere)")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> invalidateAll(@PathVariable Long userId) {
        adminSessionService.invalidateAll(userId);
        
        return ResponseEntity.noContent().build();
    }
}
