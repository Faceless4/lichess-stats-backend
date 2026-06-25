package com.autotests.lichessbackend.dto;

public record LoginRequest(
        String username,
        String password
) {
}