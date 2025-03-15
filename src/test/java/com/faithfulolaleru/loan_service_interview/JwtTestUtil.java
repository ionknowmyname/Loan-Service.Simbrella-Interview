package com.faithfulolaleru.loan_service_interview;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class JwtTestUtil {
    private static final String SECRET_KEY = "504E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    // Use same key as in production

    private static Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String getValidToken() {
        return Jwts.builder()
                .setSubject("testUser@example.com") // Set the username or user ID
                .claim("role", "ROLE_USER") // Add user roles
                // .setIssuedAt(new Date())
                // .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1-hour expiration
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                // .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
