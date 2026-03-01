package com.ren.CatQuest.auth.controller;

import com.ren.CatQuest.auth.dto.*;
import com.ren.CatQuest.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

//    @PostMapping("/verify-email")
//    public void verifyEmail(@Valid @RequestBody EmailVerificationRequest request) {
//        authService.verifyEmail(request);
//    }
//
//    @PostMapping("/refresh")
//    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
//        return authService.refreshToken(request);
//    }
}
