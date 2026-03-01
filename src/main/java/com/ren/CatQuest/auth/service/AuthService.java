package com.ren.CatQuest.auth.service;

import com.ren.CatQuest.auth.dto.AuthResponse;
import com.ren.CatQuest.auth.dto.LoginRequest;
import com.ren.CatQuest.auth.dto.RegisterRequest;
import com.ren.CatQuest.security.model.SecurityUser;
import com.ren.CatQuest.user.entity.User;
import com.ren.CatQuest.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;


    public AuthResponse register(RegisterRequest request) {

        User user = userService.createUser(
                request.email(),
                request.password(),
                request.fullName(),
                request.phone()
        );

        /*
         If email verification is required:
         - DO NOT generate tokens here
         - Instead send verification email
         - Enable user later
         */

        SecurityUser securityUser = new SecurityUser(user);

        return generateTokens(securityUser);
    }


    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        return generateTokens(securityUser);
    }

    @Transactional(readOnly = true)
    public AuthResponse refresh(SecurityUser user) {

        return generateTokens(user);
    }

    private AuthResponse generateTokens(SecurityUser user) {

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken
        );
    }
}