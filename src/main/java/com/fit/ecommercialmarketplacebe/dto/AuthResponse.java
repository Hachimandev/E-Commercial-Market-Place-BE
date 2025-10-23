package com.fit.ecommercialmarketplacebe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private String fullName;
    private String phone;
    private String address;
    private long expiresAt;
}