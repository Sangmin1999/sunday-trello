package com.sparta.sunday.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

    private final String secretKey;

    public JwtUtil(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String extractRole(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
        return claims.get("role", String.class);
    }
}
