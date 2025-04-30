package com.employee.controller;

import com.employee.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (authService.authenticate(username, password)) {
            return ResponseEntity.ok(Map.of(
                "token", username,
                "role", authService.getUserByUsername(username).get().getRole()
            ));
        }

        return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
    }
} 