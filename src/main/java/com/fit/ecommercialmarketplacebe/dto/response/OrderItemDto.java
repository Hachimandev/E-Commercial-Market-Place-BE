package com.fit.ecommercialmarketplacebe.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDto {
    private String productId;
    private String productName;
    private String productImageURL;
    private int quantity;
    private double price;
}