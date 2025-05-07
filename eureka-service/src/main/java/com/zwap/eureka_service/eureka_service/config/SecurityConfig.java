
package com.zwap.eureka_service.eureka_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())  // Enable CORS with default settings
                .csrf(csrf -> csrf.disable())  // Disable CSRF (only if necessary)
                .anonymous(AbstractHttpConfigurer::disable) // Disable anonymous access
                .formLogin(form -> form.permitAll()) // Enable form login
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/eureka/**", "/actuator/**").permitAll() // Allow public access to specific endpoints
                        .anyRequest().authenticated() // Require authentication for all other requests
                );

        return http.build();
    }

}