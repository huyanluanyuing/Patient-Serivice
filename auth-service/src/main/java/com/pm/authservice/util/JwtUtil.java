package com.pm.authservice.util;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class JwtUtil {
    private final Key secretKey;
    public JwtUtil(@Value("${jwt.secret}")String secret) {
        // Decode the Base64-encoded secret key
        byte[] keyBytes = Base64.getDecoder().decode(secret.getBytes(
                StandardCharsets.UTF_8));
        // Generate the Key object
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

}
