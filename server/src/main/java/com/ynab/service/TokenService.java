package com.ynab.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ynab.model.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenService {
    // FIXME: GENERATE AN ACTUAL KEY AND PUT IT IN A CONFIG FILE
    //        AND MAKE SURE THAT IT ISN'T CHECKED INTO GITHUB
    private String JWT_SECRET = "SUPER_SECRET_KEY";

    public String generateAccessToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("email", user.getEmail())
                .withExpiresAt(genAccessExpirationDate())
                .sign(algorithm);
        } catch (JWTCreationException exception) {
            return null;
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public String recoverToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("jwt"))
                return cookie.getValue();
        }
        return null;
    }

    public String extractEmailFromToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("email").asString();
        } catch (JWTDecodeException exception) {
            return null;
        }
    }

    private Instant genAccessExpirationDate() {
        return LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-05:00"));
    }
}
