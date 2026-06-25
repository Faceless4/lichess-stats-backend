package com.autotests.lichessbackend.dto;

import com.autotests.lichessbackend.util.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username,
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
        String password,
        Role role
) {
}