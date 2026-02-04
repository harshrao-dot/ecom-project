package org.harshdev.ecom.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    // 1. Ek Secret Key banao (Ye sirf server ko pata honi chahiye)
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 2. Token ki validity (Jaise 1 din = 86400000 ms)
    private final long expirationTime = 86400000;

    // Method: Token generate karne ke liye
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key) // Key se sign kar diya taaki koi nakal na kar sake
                .compact();
    }

    // Method: Token se username nikalne ke liye
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Method: Token validate karne ke liye
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // Token expire ho gaya ya galat h
        }
    }
}