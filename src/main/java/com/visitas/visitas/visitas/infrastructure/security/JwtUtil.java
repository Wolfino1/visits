package com.visitas.visitas.visitas.infrastructure.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Long extractSellerId(String token) {
        Object claim = extractAllClaims(token).get("sellerId");
        if (claim instanceof Number) {
            return ((Number) claim).longValue();
        } else if (claim instanceof String) {
            return Long.valueOf((String) claim);
        }
        return null;
    }

    public Long extractBuyerId(String token) {
        Object claim = extractAllClaims(token).get("buyerId");
        if (claim instanceof Number) {
            return ((Number) claim).longValue();
        } else if (claim instanceof String) {
            return Long.valueOf((String) claim);
        }
        return null;
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
