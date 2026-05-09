package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DocumentRequest(
        @NotBlank(message = "Title cannot be blank")
        String title,

        @NotBlank(message = "Content cannot be blank")
        String content) {
}
