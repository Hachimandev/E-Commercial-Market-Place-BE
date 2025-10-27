package com.fit.ecommercialmarketplacebe.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ProductDetailVariantDto {
    private String id;
    private String name;
    private String shortDescription;
    private double price;
    private double rating;
    private String offer;
    private String mainImageURL; // Ảnh chính
    private List<ProductImageDto> images; // Các ảnh phụ
    private List<ProductColorDto> colors;
    private List<String> sizes;
}