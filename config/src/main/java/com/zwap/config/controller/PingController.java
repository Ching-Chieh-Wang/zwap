package com.zwap.config.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @PostMapping("/ping")
    public ResponseEntity<String> receivePing() {

        return ResponseEntity.ok("pong");
    }
}
