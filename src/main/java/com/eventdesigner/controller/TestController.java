package com.eventdesigner.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "Event Experience Designer Backend is running successfully!";
    }

    @GetMapping("/api/health")
    public String health() {
        return "Backend is healthy!";
    }
}
