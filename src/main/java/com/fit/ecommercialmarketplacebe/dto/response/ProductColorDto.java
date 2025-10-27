package com.fit.ecommercialmarketplacebe.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductColorDto {
    private String id;
    private String code; // Mã hex

    // Helper nhỏ để đọc metadata
    public static String parseColorCode(String metadata, ObjectMapper objectMapper) {
        if (metadata == null || metadata.isEmpty()) return "#FFFFFF"; // Mã màu trắng mặc định
        try {
            JsonNode root = objectMapper.readTree(metadata);
            return root.path("code").asText("#FFFFFF");
        } catch (JsonProcessingException e) {
            return "#FFFFFF";
        }
    }
}