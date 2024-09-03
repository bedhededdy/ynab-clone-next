package com.ynab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ynab.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        // For now, assume that calling logged in with a valid cookie is an error
        if (userService.getUserFromRequest(request) != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        if (loginRequest.email == null || loginRequest.email.isBlank() || loginRequest.password == null || loginRequest.password.isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        String jwt = userService.login(loginRequest.email, loginRequest.password);
        if (jwt == null || jwt.isBlank())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        // FIXME: NEED TO SET HTTPONLY, SECURE TO TRUE AND MAKE SURE THAT
        //        CROSS-SITE COOKIE IS ALLOWED
        //        THIS MAY REQUIRE SOME MESSING WITH THE CORS AND SECURITY
        //        CONFIGURATION TO GET TO WORK
        var cookie = new Cookie("jwt", jwt);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().body("User logged in");
    }

    @Data
    private static class RegisterRequest {
        private String email;
        private String password;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest.email == null || registerRequest.email.isBlank() || registerRequest.password == null || registerRequest.password.isBlank())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        try {
            userService.register(registerRequest.email, registerRequest.password);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // FIXME: MAY NEED TO SET COOKIE AS HTTP ONLY IN PROD
        var cookie = new Cookie("jwt", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().body("User logged out");
    }
}
