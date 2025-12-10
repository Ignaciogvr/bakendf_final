package com.huertohogar.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && !header.isBlank()) {

            // 🔥 Arreglar tokens mal codificados de Postman o Android
            header = URLDecoder.decode(header, StandardCharsets.UTF_8)
                    .replace("Bearer%20", "Bearer ")
                    .replace("Bearer+", "Bearer ")
                    .replaceAll("\\s+", " ") // limpia espacios dobles
                    .trim();

            // 🔥 Si el header viene como "bearer " en minúsculas → arreglar
            if (header.toLowerCase().startsWith("bearer ")) {
                header = "Bearer " + header.substring(7);
            }

            if (header.startsWith("Bearer ")) {

                String token = header.substring(7).trim();

                try {

                    if (jwtUtil.validate(token)) {

                        String email = jwtUtil.extractEmail(token);

                        UserDetails userDetails =
                                userDetailsService.loadUserByUsername(email);

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }

                } catch (Exception e) {
                    response.sendError(401, "Token inválido: " + e.getMessage());
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
