/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.Security;

import com.anshul.projects.AirBnb.entity.User;
import com.anshul.projects.AirBnb.service.Impl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    // Service used to read & validate JWT
    private final JWTService jwtService;

    // Service used to fetch user from DB
    private final UserServiceImpl userService;

    // Used to send proper error response instead of crashing app
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1️⃣ Read Authorization header
            final String requestTokenHeader = request.getHeader("Authorization");

            // 2️⃣ If header is missing OR not starting with Bearer → skip JWT logic
            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 3️⃣ Remove "Bearer " from token
            String token = requestTokenHeader.substring(7);

            // 4️⃣ Extract userId from JWT token
            Long userId = jwtService.getUserIdFromToken(token);

            // 5️⃣ If userId is valid and no authentication exists yet
            if (userId != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // 6️⃣ Load user from database
                User user = userService.getUserById(userId);

                // 7️⃣ Create authentication object
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // 8️⃣ Attach request details (IP, session, etc.)
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 9️⃣ Store authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            // 🔟 Continue request to controller
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            // ❌ If ANY error happens (invalid token, expired token, etc.)
            // this sends proper JSON error instead of crashing the server
            handlerExceptionResolver.resolveException(
                    request,
                    response,
                    null,
                    ex
            );
        }
    }
}
