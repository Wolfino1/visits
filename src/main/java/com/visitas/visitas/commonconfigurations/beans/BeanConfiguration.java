package com.visitas.visitas.commonconfigurations.beans;

import com.visitas.visitas.visitas.domain.ports.in.VisitsServicePort;
import com.visitas.visitas.visitas.domain.ports.out.HouseClientPort;
import com.visitas.visitas.visitas.domain.ports.out.VisitsPersistencePort;
import com.visitas.visitas.visitas.domain.usecases.VisitsUseCase;
import com.visitas.visitas.visitas.infrastructure.adapters.persistence.mysql.VisitsPersistenceAdapter;
import com.visitas.visitas.visitas.infrastructure.mappers.VisitsEntityMapper;
import com.visitas.visitas.visitas.infrastructure.repositories.mysql.VisitsRepository;
import com.visitas.visitas.visitas.infrastructure.security.JwtAuthenticationFilter;
import com.visitas.visitas.visitas.infrastructure.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class BeanConfiguration {
    private final JwtUtil jwtUtil;
    private final VisitsRepository visitsRepository;
    private final VisitsEntityMapper visitsEntityMapper;
    private final HouseClientPort houseClientPort;

    @Bean
    public VisitsPersistencePort visitsPersistencePort() {
        return new VisitsPersistenceAdapter(visitsRepository, visitsEntityMapper);
    }

    @Bean
    public VisitsServicePort visitsServicePort(VisitsPersistencePort persistencePort) {
        return new VisitsUseCase(persistencePort,houseClientPort);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Hacer público el GET /get
                        .requestMatchers(HttpMethod.GET, "/api/v1/visits/get").permitAll()
                        // 2. Proteger el POST /visits solo para SELLER
                        .requestMatchers(HttpMethod.POST, "/api/v1/visits").hasRole("SELLER")
                        // 3. Cualquier otra petición necesita estar autenticada
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, exAuth) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado"))
                        .accessDeniedHandler((req, res, exDenied) ->
                                res.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos"))
                );
        return http.build();
    }
}
