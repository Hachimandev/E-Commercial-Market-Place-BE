package com.fit.ecommercialmarketplacebe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String address;

    private String avatarURL;
    @Enumerated(EnumType.STRING)
    private Role role;
}
