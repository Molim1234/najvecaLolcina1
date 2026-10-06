package com.example.najvecaLolcina.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWtServicee {

    @Value("${jwt.secret}")
    private String secret;

    // ==========================================
    // CREATE TOKEN
    // ==========================================

    public String createToken(String name) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims(claims)
                .subject(name)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key())
                .compact();
    }

    // ==========================================
    // GET SECRET KEY
    // ==========================================

    private SecretKey key() {
        byte[] bytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }

    // ==========================================
    // EXTRACT USERNAME / SUBJECT
    // ==========================================

    public String returnName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Možeš čak nazvati ovu metodu extractUsername
    // ako želiš standardniji naziv.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ==========================================
    // EXTRACT SINGLE CLAIM
    // ==========================================

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ==========================================
    // EXTRACT ALL CLAIMS
    // ==========================================

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ==========================================
    // CHECK TOKEN EXPIRATION
    // ==========================================

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ==========================================
    // GET EXPIRATION DATE
    // ==========================================

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ==========================================
    // VALIDATE TOKEN
    // ==========================================

    public boolean validateToken(String token, UserDetails userDetails) {

        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }
}