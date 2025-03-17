package com.example.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    private final Key key;
    private final JwtParser jwtParser;

    public JwtUtil(@Value("${security.jwt.base64-secret-key}") String base64Secret) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException ignored) {
        }
        return false;
    }

    public Claims parseJwtClaims(String token) {

        return jwtParser.parseClaimsJws(token).getBody();
    }

}
