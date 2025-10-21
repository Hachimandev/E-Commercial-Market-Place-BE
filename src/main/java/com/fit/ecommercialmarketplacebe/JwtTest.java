package com.fit.ecommercialmarketplacebe;



import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTest {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    @PostConstruct
    public void testJwtGeneration() {
        try {
            // ✅ Tạo key từ secret
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            // ✅ Sinh JWT token mẫu
            String token = Jwts.builder()
                    .setSubject("testUser")
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            System.out.println("🔑 JWT Token tạo thành công:");
            System.out.println(token);

            // ✅ Giải mã kiểm tra
            String subject = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            System.out.println("✅ Giải mã thành công, user: " + subject);
        } catch (Exception e) {
            System.err.println("❌ Lỗi JWT: " + e.getMessage());
        }
    }
}

