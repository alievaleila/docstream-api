package com.example.dto.response;

import com.example.domain.enums.AuditAction;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        AuditAction action,
        String actor,
        String details,
        LocalDateTime timestamp
) {
}
