package com.ren.CatQuest.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record EmailVerificationRequest(
        @NotBlank
        String token
) {}