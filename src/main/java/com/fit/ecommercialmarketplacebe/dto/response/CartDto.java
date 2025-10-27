package com.fit.ecommercialmarketplacebe.dto.response;
import lombok.Builder;
import lombok.Data;
import java.util.List;
@Data
@Builder
public class CartDto {
    private String cartId;
    private double totalPrice;
    private List<CartItemDto> items;
}