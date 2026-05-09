package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RejectRequest(
        @NotBlank(message = "Rejection comment cannot be blank")
        String comment
) {}
