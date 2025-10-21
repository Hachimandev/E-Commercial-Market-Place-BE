package com.fit.ecommercialmarketplacebe.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String password;
    private String phone;
    private String address;
    private String role;
}
