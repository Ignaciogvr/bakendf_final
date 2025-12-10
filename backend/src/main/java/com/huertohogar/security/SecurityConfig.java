package com.huertohogar.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            // ====================================================
            //                 🔥 CORS FIX DEFINITIVO
            // ====================================================
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                corsConfig.setAllowCredentials(true);
                corsConfig.addAllowedOrigin("http://localhost:3000");
                corsConfig.addAllowedHeader("*");
                corsConfig.addAllowedMethod("*");
                return corsConfig;
            }))

            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, exc) -> {
                res.sendError(401, "Unauthorized: token inválido o ausente");
            }))

            .authorizeHttpRequests(auth -> auth

                // ====================================================
                //     ⚠️ PERMITIR OPTIONS PARA REACT
                // ====================================================
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ====================================================
                //     🔓 ENDPOINTS PÚBLICOS
                // ====================================================
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/register").permitAll()
                .requestMatchers("/api/auth/create-admin").permitAll()
                .requestMatchers("/img/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/productos").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()

                // ====================================================
                //     🔐 ENDPOINTS SOLO ADMIN
                // ====================================================
                .requestMatchers("/api/usuarios/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/pedidos").hasRole("ADMIN")
                .requestMatchers("/api/pedidos/admin/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")

                // ====================================================
                //     👤 ENDPOINTS PARA CLIENTES LOGUEADOS
                // ====================================================
                .requestMatchers("/api/usuarios/perfil/**").authenticated()
                .requestMatchers("/api/usuarios/actualizar/**").authenticated()
                .requestMatchers("/api/usuarios/cambiar-password/**").authenticated()

                .requestMatchers("/api/pedidos/usuario/**").authenticated()

                .anyRequest().permitAll()
            )

            .userDetailsService(customUserDetailsService)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

            .logout(logout -> logout.disable());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
