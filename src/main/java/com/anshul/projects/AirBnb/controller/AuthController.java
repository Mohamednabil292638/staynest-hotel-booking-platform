/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.controller;

import com.anshul.projects.AirBnb.dto.LoginDto;
import com.anshul.projects.AirBnb.dto.LoginResponseDto;
import com.anshul.projects.AirBnb.dto.SignUpRequestDto;
import com.anshul.projects.AirBnb.dto.UserDto;
import com.anshul.projects.AirBnb.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "1. Auth", description = """
        **Demo logins** (pre-filled in login body):
        - Guest: `guest@demo.com` / `Demo@123`
        - Hotel Manager (admin): `manager@demo.com` / `Demo@123`
        """)
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Sign up (creates GUEST only)")
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request) {
        return new ResponseEntity<>(authService.signUp(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Login → get JWT", description = """
            Execute with pre-filled demo credentials. Copy `accessToken` from response, then click **Authorize** (top right) and paste the token.
            Use **manager@demo.com** for admin `/admin/*` APIs.
            """)
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginDto loginDto,
            HttpServletResponse response) {

        String[] tokens = authService.login(loginDto);

        Cookie refreshCookie = new Cookie("refreshToken", tokens[1]);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshCookie);

        return ResponseEntity.ok(new LoginResponseDto(tokens[0]));
    }

    @Operation(summary = "Refresh access token (uses cookie)")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request) {

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() ->
                        new AuthenticationServiceException("Refresh token not found"));

        String newAccessToken = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(new LoginResponseDto(newAccessToken));
    }

    @Operation(summary = "Logout")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        Arrays.stream(request.getCookies())
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .ifPresent(cookie -> {
                    cookie.setValue(null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                });

        return ResponseEntity.ok("Logged out successfully");
    }
}
