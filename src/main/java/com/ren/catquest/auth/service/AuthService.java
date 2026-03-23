package com.ren.catquest.auth.service;

import com.ren.catquest.auth.dto.AuthResponse;
import com.ren.catquest.auth.dto.LoginRequest;
import com.ren.catquest.auth.dto.RefreshTokenRequest;
import com.ren.catquest.auth.dto.RegisterRequest;
import com.ren.catquest.security.jwt.JwtService;
import com.ren.catquest.security.model.SecurityUser;
import com.ren.catquest.user.entity.User;
import com.ren.catquest.user.repository.UserRepository;
import com.ren.catquest.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    private final UserRepository userRepo;


    public AuthResponse register(RegisterRequest request) {

        User user = userService.createUser(request);

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

    public AuthResponse logout(String accessToken) {
        return new AuthResponse("", "");
    }

    public AuthResponse deleteCurrentUser(String accessToken) {
        Long userId = jwtService.extractUserId(accessToken);
        if (userId == null) throw new RuntimeException("Invalid token");

        userService.deleteUser(userId);

        return new AuthResponse("", "");
    }

    @Transactional(readOnly = true)
    public AuthResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        String username = jwtService.extractUsername(refreshToken);

        if (username == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        SecurityUser user = (SecurityUser) userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

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