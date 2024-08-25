package com.ynab.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ynab.service.UserService;

import lombok.Data;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Data
    private static class LoginRequest {
        private String email;
        private String password;
    }

    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.email == null || loginRequest.email.isBlank() || loginRequest.password == null || loginRequest.password.isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Long userId = userService.login(loginRequest.email, loginRequest.password);
        if (userId == -1L)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(userId);
    }

    @Data
    private static class RegisterRequest {
        private String email;
        private String password;
    }

    @PostMapping("/signup")
    public ResponseEntity<Long> register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest.email == null || registerRequest.email.isBlank() || registerRequest.password == null || registerRequest.password.isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Long userId = userService.register(registerRequest.email, registerRequest.password);
        if (userId == -1L)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }
}
