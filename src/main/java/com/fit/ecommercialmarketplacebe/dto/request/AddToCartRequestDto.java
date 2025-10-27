package com.fit.ecommercialmarketplacebe.dto.request;
import lombok.Data;
@Data
public class AddToCartRequestDto {
    private Long productId;
    private int quantity;
    // Thêm các trường biến thể nếu cần, ví dụ:
    // private String color;
    // private String size;
}