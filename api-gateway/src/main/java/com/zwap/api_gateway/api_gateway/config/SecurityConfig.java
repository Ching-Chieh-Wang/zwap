package com.zwap.api_gateway.api_gateway.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity // Enables Spring Security for WebFlux applications
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    @Value("${firebase.project-id}") // This will look for 'firebase.project-id' in properties/env
    private String FIREBASE_PROJECT_ID;
    private final String JWT_SET_URI = "https://www.googleapis.com/oauth2/v3/certs";

    @PostConstruct
    public void logFirebaseProjectId() {
        log.info("Firebase Project ID loaded: {}", FIREBASE_PROJECT_ID);
    }

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
                        oauth2.jwt(jwtSpec -> jwtSpec.jwtDecoder(firebaseJwtDecoder()))
                );// Configure JWT validation

        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder firebaseJwtDecoder() {
        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(JWT_SET_URI).build();
        jwtDecoder.setJwtValidator(
                JwtValidators.createDefaultWithIssuer("https://securetoken.google.com/" + FIREBASE_PROJECT_ID)
        );

        return jwtDecoder;
    }
}