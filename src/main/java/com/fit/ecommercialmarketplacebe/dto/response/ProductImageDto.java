package com.fit.ecommercialmarketplacebe.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImageDto {
    private String id;
    private String imageURL;
}