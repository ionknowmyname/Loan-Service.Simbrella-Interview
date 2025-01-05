package com.faithfulolaleru.loan_service_interview.config.jwt;

import com.faithfulolaleru.loan_service_interview.dto.LoginResponse;
import com.faithfulolaleru.loan_service_interview.entity.User;
import com.faithfulolaleru.loan_service_interview.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public record JwtService(UserService userService) {

    private static final String SECRET_KEY = "504E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /*public LoginResponse generateToken(UserDetails userDetails) {
        User user = (User) userDetails;
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getUserRole().name());
        return generateToken(claims, userDetails);
    }*/

    public LoginResponse generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        String token = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  // 24 hrs in milliseconds
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        final Instant expirationDateInMillis = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24).toInstant();
        final Instant expirationDateInMillis2 = extractExpiration(token).toInstant();

        return LoginResponse.builder()
                .accessToken(token)
                .refreshToken(token)
                .expiresIn(expirationDateInMillis)
                .build();
    }

    public LoginResponse generateToken(Authentication authentication) {
        // UserDetails appUser = (UserDetails) authentication.getPrincipal();
        User appUser = userService.findUserByEmail(authentication.getName());

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", appUser.getUserRole().name());

        /*List<String> roles = appUser.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("role", roles);*/

        String token = Jwts
            .builder()
            .setClaims(claims)
            .setSubject(appUser.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  // 24 hrs in milliseconds
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();

        final Instant expirationDateInMillis = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24).toInstant();
        final Instant expirationDateInMillis2 = extractExpiration(token).toInstant();

        return LoginResponse.builder()
                .accessToken(token)
                .refreshToken(token)
                .expiresIn(expirationDateInMillis)
                // .tokenFor(authentication.getName())
                .build();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
