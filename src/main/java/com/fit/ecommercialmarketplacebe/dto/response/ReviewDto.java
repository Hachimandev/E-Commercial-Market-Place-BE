package com.fit.ecommercialmarketplacebe.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDto {
    private String id;
    private String user; // Tên của buyer (fullName)
    private String date; // Sẽ được xử lý ở service
    private String comment;
    private String userImage; // avatarURL của buyer
}