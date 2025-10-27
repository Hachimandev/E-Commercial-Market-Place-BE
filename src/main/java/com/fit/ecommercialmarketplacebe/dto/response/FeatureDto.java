package com.fit.ecommercialmarketplacebe.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeatureDto {
    private String id;
    private String icon;
    private String text;
}