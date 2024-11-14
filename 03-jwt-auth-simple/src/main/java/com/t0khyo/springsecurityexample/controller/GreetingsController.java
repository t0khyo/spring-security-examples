package com.t0khyo.springsecurityexample.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
public class GreetingsController {
    @GetMapping("/public")
    public String publicPage() {
        return "Hello! this is a public endpoint 📢🌐";
    }

    @GetMapping("/private")
    public String privatePage(Authentication authentication) {
        return MessageFormat.format("Hello, {0}! this is a vip endpoint 🔒😎", authentication.getName());
    }
}
