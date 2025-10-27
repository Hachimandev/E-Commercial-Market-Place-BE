package com.fit.ecommercialmarketplacebe.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ProductDetailGeneralDto {
    private String id;
    private String name;
    private double price;
    private double rating;
    private int reviewCount;
    private String description;
    private String imageURL; // Ảnh chính
    private List<FeatureDto> features;
    private List<ReviewDto> reviews;
    private List<RelevantProductDto> relevantProducts;
}