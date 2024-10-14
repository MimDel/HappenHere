package com.example.happenhere.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils {
    private final String SECRET = "secret";
    private final long EXPIRES = 1000 * 60 * 60 * 24 *7; //1 week

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRES))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public boolean validateToken(String token, String username) {
        var decodedToken = JWT.decode(token);
        return decodedToken.getSubject().equals(username) && decodedToken.getExpiresAt().before(new Date(System.currentTimeMillis()));
    }
    public String getUsernameFromToken(String token) {
        return JWT.decode(token).getSubject();
    }
}
