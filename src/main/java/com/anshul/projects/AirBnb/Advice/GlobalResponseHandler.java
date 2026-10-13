/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.Advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    private static final List<String> ALLOWED_ROUTES = List.of(
            "/v3/api-docs",
            "/swagger-ui",
            "/actuator"
    );

    @Override
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return true; // Let Spring call beforeBodyWrite
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {

        String path = request.getURI().getPath();

        boolean isAllowedRoute = ALLOWED_ROUTES
                .stream()
                .anyMatch(path::contains);

        // ❌ Do NOT wrap swagger / actuator / already wrapped responses
        if (body instanceof ApiResponse<?> || body instanceof ResponseEntity || isAllowedRoute) {
            return body;
        }

        // ✅ Wrap normal success responses
        return new ApiResponse<>(body);
    }
}
