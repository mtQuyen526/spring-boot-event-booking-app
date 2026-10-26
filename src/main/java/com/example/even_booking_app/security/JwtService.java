package com.example.even_booking_app.security;

import com.example.even_booking_app.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationSeconds;
    private final String issuer;


    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-seconds}") long expirationSeconds,
            @Value("${app.jwt.issuer:event-booking-app-api}") String issuer) {

        this.signingKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secret)
        );
        this.expirationSeconds = expirationSeconds;
        this.issuer = issuer;
    }


    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationSeconds);

        // Roles
        List<String> roles = user.getRoles() == null || user.getRoles().isBlank() ? List.of()
                : Arrays.stream(user.getRoles().split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .toList();
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .addClaims(claims)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Instant getExpiration(String token) {
        return parseClaims(token)
                .getExpiration()
                .toInstant();
    }
}
