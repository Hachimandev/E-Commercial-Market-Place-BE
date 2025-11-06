package com.fit.ecommercialmarketplacebe.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Integer id;
    private String fullName;
    private String username;
    private String avatarURL;
    private String status;
    private String role;
}