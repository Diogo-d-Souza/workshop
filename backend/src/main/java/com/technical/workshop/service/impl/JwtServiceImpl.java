package com.technical.workshop.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.technical.workshop.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtServiceImpl {
    @Value("${api.security.token.secret}")
    private String secret;

    public String createToken(User user) {
        try {
            return JWT.create()
                    .withIssuer("workshop")
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withExpiresAt(LocalDateTime.now().plusMinutes(10)
                            .toInstant(ZoneOffset.of("-03:00"))
                    ).sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while trying to generate a token", exception);
        }
    }

    public String tokenValidator(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret)).withIssuer("workshop")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }
}
