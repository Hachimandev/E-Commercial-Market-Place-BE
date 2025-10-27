package com.fit.ecommercialmarketplacebe.repository;

import com.fit.ecommercialmarketplacebe.entity.Buyer;
import com.fit.ecommercialmarketplacebe.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByBuyer(Buyer buyer);
}