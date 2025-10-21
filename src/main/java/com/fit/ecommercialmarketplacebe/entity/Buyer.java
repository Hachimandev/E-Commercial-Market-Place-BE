package com.fit.ecommercialmarketplacebe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Buyer extends User {

    @OneToOne(mappedBy = "buyer", cascade = CascadeType.ALL)
    private Cart cart;
}
