package com.ren.CatQuest.auth.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {}