package com.zwap.api_gateway.api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @PostMapping("/ping")
    public ResponseEntity<String> receivePing() {

        return ResponseEntity.ok("pong");
    }
}
