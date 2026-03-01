package com.ren.CatQuest.auth.service;

import com.ren.CatQuest.security.jwt.JwtService;
import com.ren.CatQuest.security.jwt.JwtTokenType;
import com.ren.CatQuest.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;

    public String generateAccessToken(SecurityUser user) {
        return jwtService.generateToken(user, JwtTokenType.ACCESS);
    }

    public String generateRefreshToken(SecurityUser user) {
        return jwtService.generateToken(user, JwtTokenType.REFRESH);
    }
}