package com.zwap.eureka_service.eureka_service.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // you can keep CSRF on if you want formLogin + logout to work securely
                .csrf(csrf -> csrf.disable())

                // 1) public endpoints
                .authorizeHttpRequests(auth -> auth

                        // static assets, login/logout pages must be open
                        .requestMatchers(
                                "/css/**", "/js/**", "/images/**",
                                "/webjars/**",
                                "/admin/assets/**",
                                "/admin/login", "/admin/logout",
                                "/eureka/**"
                        ).permitAll()

                        // everything else—including /, /admin/**, /eureka/**, /actuator/**—requires auth
                        .anyRequest().authenticated()
                )

                // 2) form login + basic auth
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .defaultSuccessUrl("/admin", true)
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults())

                // 3) logout support
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .permitAll()
                );

        return http.build();
    }

}
