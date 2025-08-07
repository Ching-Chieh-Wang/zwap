package com.zwap.api_gateway_service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity // Enables Spring Security for WebFlux applications
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Disable CSRF for API Gateway (stateless REST APIs)
                .authorizeExchange(exchanges ->
                        exchanges
                                // Public/Unauthenticated Endpoints
                                .pathMatchers("/auth/**").permitAll() // Example: /auth/login, /auth/register
                                .pathMatchers("/public/**").permitAll() // Example: /public/products, /public/news
                                .pathMatchers("/actuator/**").permitAll() // Health checks, info (if you want them public)
                                // Authenticated Endpoints
                                .anyExchange().authenticated() // All other requests require authentication
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                );

        return http.build();
    }

}