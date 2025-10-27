package com.fit.ecommercialmarketplacebe.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RelevantProductDto {
    private String id;
    private String name;
    private double rating;
    private double price;
    private String image; // Chỉ imageURL
}