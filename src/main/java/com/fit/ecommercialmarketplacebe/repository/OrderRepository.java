package com.fit.ecommercialmarketplacebe.repository;

import com.fit.ecommercialmarketplacebe.entity.Buyer;
import com.fit.ecommercialmarketplacebe.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findOrderByOrderId(Long orderId);
    List<Order> findOrderByBuyerUserId(Integer buyer_userId);
    List<Order> findByBuyerOrderByOrderDateDesc(Buyer buyer);
}