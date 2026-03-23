package com.ren.catquest.security.jwt;

import com.ren.catquest.security.model.SecurityUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final Key key;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration-ms}") long accessExp,
            @Value("${jwt.refresh-expiration-ms}") long refreshExp
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpirationMs = accessExp;
        this.refreshTokenExpirationMs = refreshExp;
    }

    public String generateToken(SecurityUser user, JwtTokenType type) {
        long expiration =
                type == JwtTokenType.ACCESS
                        ? accessTokenExpirationMs
                        : refreshTokenExpirationMs;

        Instant now = Instant.now();
        Instant exp = now.plusMillis(expiration);

        // extract roles as simple string list
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("uid", user.getId())
                .claim("type", type.name())
                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, SecurityUser user) {
        return extractUsername(token).equals(user.getUsername())
                && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Instant expiration = extractClaims(token)
                .getExpiration()
                .toInstant();
        return Instant.now()
                .isAfter(expiration);
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractClaims(token).get("uid", Long.class);
    }

    // NEW: extract roles from token
    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}