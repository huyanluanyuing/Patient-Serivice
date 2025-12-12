package com.pm.authservice.service;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.model.User;
import com.pm.authservice.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService , PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        Optional<String> token = userService.findByEmail(loginRequestDTO.getEmail()) // first find the user by email
                .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(), u.getPassword())) // then check if the password matches
                .map(u -> jwtUtil.generateToken(u.getEmail(),u.getRole())); // if both are valid, generate a JWT token
        return token;
    }

    public boolean validateToken(String substring) {
        try {
            jwtUtil.validateToken(substring);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
