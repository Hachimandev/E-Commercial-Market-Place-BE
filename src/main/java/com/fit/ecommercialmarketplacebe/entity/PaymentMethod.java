package com.fit.ecommercialmarketplacebe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String provider;
    private String last4;
    private String details;
    private String iconName;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    @JsonIgnore
    private Buyer buyer;
}