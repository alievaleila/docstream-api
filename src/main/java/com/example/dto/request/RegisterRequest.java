package com.example.dto.request;

import com.example.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @Email(message = "Valid email required")
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @NotNull
        Role role
) {}