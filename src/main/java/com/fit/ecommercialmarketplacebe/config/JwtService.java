package com.fit.ecommercialmarketplacebe.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    @PostConstruct
    public void init() {
        logger.info("JWT Secret Key used (length: {})", secret != null ? secret.length() : "null");
        if (secret == null || secret.getBytes().length < 32) {
            logger.warn("WARNING: JWT Secret Key is null or potentially too short (< 32 bytes) for HS256 algorithm!");
        }
    }

    private Key getKey() {
        if (secret == null) {
            logger.error("JWT Secret Key is null! Cannot generate signing key.");
            throw new IllegalStateException("JWT Secret Key cannot be null.");
        }
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        if (username == null || username.trim().isEmpty()) {
            logger.error("Attempted to generate token with null or empty username!");
            throw new IllegalArgumentException("Username cannot be null or empty when generating token.");
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.error("Failed to parse JWT: Token has expired. {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.error("Failed to parse JWT: Unsupported token format. {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            logger.error("Failed to parse JWT: Malformed token. {}", e.getMessage());
            throw e;
        } catch (SignatureException e) {
            logger.error("Failed to parse JWT: Invalid signature (likely wrong secret key!). {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("Failed to parse JWT: Illegal argument. {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Failed to parse JWT due to unexpected error.", e);
            throw new JwtException("Failed to parse JWT token", e);
        }
    }

    public String extractUsername(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String subject = claims.getSubject();
            if (subject == null) {
                logger.warn("JWT Claim 'subject' (sub) is null in the token.");
            }
            return subject;
        } catch (JwtException e) {
            logger.error("Could not extract username due to JWT parsing error: {}", e.getMessage());
            return null;
        } catch (Exception e){
            logger.error("Unexpected error extracting username.", e);
            return null;
        }
    }

    public String extractRole(String token) {
        try {
            return (String) extractAllClaims(token).get("role");
        } catch (JwtException e) {
            logger.error("Could not extract role due to JWT parsing error: {}", e.getMessage());
            return null;
        } catch (Exception e){
            logger.error("Unexpected error extracting role.", e);
            return null;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        if (username == null) {
            logger.warn("Cannot validate token because username extraction failed.");
            return false;
        }
        boolean isUsernameMatch = username.equals(userDetails.getUsername());
        boolean isTokenNotExpired = !isTokenExpired(token);
        return (isUsernameMatch && isTokenNotExpired);
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (JwtException e) {
            logger.error("Could not check token expiration due to JWT parsing error: {}", e.getMessage());
            return true;
        } catch (Exception e){
            logger.error("Unexpected error checking token expiration.", e);
            return true;
        }
    }
}