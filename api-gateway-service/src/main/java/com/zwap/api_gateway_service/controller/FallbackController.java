package com.zwap.api_gateway_service.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class FallbackController {

    @RequestMapping("/fallback")
    public Mono<ResponseEntity<String>> fallback() {
        try {
            ClassPathResource resource = new ClassPathResource("static/fallback.html");
            String html = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            return Mono.just(
                    ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                            .contentType(MediaType.TEXT_HTML)
                            .body(html)
            );
        } catch (IOException e) {
            return Mono.just(
                    ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                            .contentType(MediaType.TEXT_PLAIN)
                            .body("Service temporarily unavailable")
            );
        }
    }
}
