package com.example.dto.response;

import com.example.domain.enums.DocumentStatus;

public record DocumentResponse(
        Long id,
        String title,
        String content,
        DocumentStatus status,
        UserResponse owner) {
}
