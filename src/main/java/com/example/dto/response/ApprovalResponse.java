package com.example.dto.response;

import com.example.domain.enums.ApprovalStatus;

public record ApprovalResponse(
        Long id,
        DocumentResponse document,
        UserResponse approver,
        ApprovalStatus status,
        String comment) {
}
