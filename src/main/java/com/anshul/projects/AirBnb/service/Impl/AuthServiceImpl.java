/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.service.Impl;

import com.anshul.projects.AirBnb.Exception.ResourceNotFoundException;
import com.anshul.projects.AirBnb.Security.JWTService;
import com.anshul.projects.AirBnb.dto.LoginDto;
import com.anshul.projects.AirBnb.dto.SignUpRequestDto;
import com.anshul.projects.AirBnb.dto.UserDto;
import com.anshul.projects.AirBnb.entity.Enums.Role;
import com.anshul.projects.AirBnb.entity.User;
import com.anshul.projects.AirBnb.repository.UserRepository;
import com.anshul.projects.AirBnb.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    // =========================
    // SIGN UP
    // =========================
    @Override
    public UserDto signUp(SignUpRequestDto dto) {

        userRepository.findByEmail(dto.getEmail())
                .ifPresent(u -> {
                    throw new RuntimeException(
                            "User already exists with email: " + dto.getEmail()
                    );
                });

        User user = modelMapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Set.of(Role.GUEST));

        user = userRepository.save(user);

        log.info("User registered successfully with email {}", user.getEmail());
        return modelMapper.map(user, UserDto.class);
    }

    // =========================
    // LOGIN
    // =========================
    @Override
    public String[] login(LoginDto loginDto) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDto.getEmail(),
                                loginDto.getPassword()
                        )
                );

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        log.info("User logged in successfully: {}", user.getEmail());

        return new String[]{accessToken, refreshToken};
    }

    // =========================
    // REFRESH TOKEN
    // =========================
    @Override
    public String refreshToken(String refreshToken) {

        Long userId = jwtService.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + userId));

        log.info("Refresh token validated for user {}", user.getEmail());

        return jwtService.generateAccessToken(user);
    }

    // =========================
    // LOGOUT
    // =========================
    @Override
    public void logout(String refreshToken) {
        // Stateless JWT → nothing to invalidate server-side
        // In production: blacklist token in Redis
        log.info("Logout requested");
    }
}
