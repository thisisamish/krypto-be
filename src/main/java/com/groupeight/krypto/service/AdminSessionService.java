package com.groupeight.krypto.service;

import com.groupeight.krypto.dto.ActiveSessionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AdminSessionService {
    List<ActiveSessionDto> byUser(Long userId);
    void invalidate(String sessionId);
    void invalidateAll(Long userId);
}
