package com.ren.catquest.auth.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {}