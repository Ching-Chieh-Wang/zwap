package com.zwap.config.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    private static final Logger log = LoggerFactory.getLogger(PingController.class);

    @PostMapping("/ping")
    public ResponseEntity<String> receivePing() {
        System.out.println("PingController.receivePing");
        return ResponseEntity.ok("pong");
    }
}
