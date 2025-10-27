package com.fit.ecommercialmarketplacebe.dto.response;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class PaymentMethodDto {
    private String id;
    private String type;
    private String brand;
    private String last4;
    private String email;
    private String iconName; // Tên file ảnh (ví dụ "visa-logo.png")
}