package com.visitas.visitas.commonconfigurations.beans;

import com.visitas.visitas.visitas.infrastructure.security.JwtAuthenticationFilter;
import com.visitas.visitas.visitas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/visits/**", config);
        return source;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain visitsPublicChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/visits/get")
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll()
                );
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain visitsProtectedChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/visits/**")
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/visits/get").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/visits").hasRole("SELLER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/visits/schedule").hasRole("BUYER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, exAuth) ->
                                res.sendError(HttpStatus.UNAUTHORIZED.value(), "Token inválido o expirado"))
                        .accessDeniedHandler((req, res, exDenied) ->
                                res.sendError(HttpStatus.FORBIDDEN.value(), "No tienes permisos"))
                );
        return http.build();
    }
}
