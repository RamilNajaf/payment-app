package com.example.userms.util;

import com.example.userms.enitty.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
@Component
public class JwtUtil {

    private static final String USER_ID = "user_id";
    private Key key ;

    @Value("${security.jwt.validity-in-seconds}")
    private long tokenValidityInSeconds;

    public JwtUtil(@Value("${security.jwt.base64-secret-key}") String base64Secret) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
    }

    public String createToken(User user) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + (this.tokenValidityInSeconds * 1000));
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim(USER_ID, user.getId())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }
}
