package com.ren.catquest.auth.service;

import com.ren.catquest.security.jwt.JwtService;
import com.ren.catquest.security.jwt.JwtTokenType;
import com.ren.catquest.security.model.SecurityUser;
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