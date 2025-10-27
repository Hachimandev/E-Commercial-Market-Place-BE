package com.fit.ecommercialmarketplacebe.dto.response;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class CartItemDto {
    private String id; // id của CartItem
    private int quantity;
    private double subtotal;
    private String productId;
    private String name;
    private double price; // Giá của 1 sản phẩm
    private String imageURL;
}
