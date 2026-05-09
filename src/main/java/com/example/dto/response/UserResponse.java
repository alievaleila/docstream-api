package com.example.dto.response;

import com.example.domain.enums.Role;

public record UserResponse(Long id, String email, Role role) {
}
