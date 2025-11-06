package com.fit.ecommercialmarketplacebe.repository;

import com.fit.ecommercialmarketplacebe.entity.Buyer;
import com.fit.ecommercialmarketplacebe.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findOrderByOrderId(Long orderId);
    List<Order> findOrderByBuyerUserId(Integer buyer_userId);
    List<Order> findByBuyerOrderByOrderDateDesc(Buyer buyer);

    @Query("SELECT o FROM Order o JOIN o.items oi WHERE oi.product.seller.userId = :sellerId GROUP BY o.orderId")
    List<Order> findOrdersBySellerId(@Param("sellerId") Integer sellerId);

    @Query("SELECT COUNT(DISTINCT o.orderId) FROM Order o JOIN o.items oi WHERE oi.product.seller.userId = :sellerId")
    long countBySeller(@Param("sellerId") Integer sellerId);

    @Query("SELECT o FROM Order o JOIN o.items oi WHERE o.orderId = :orderId AND oi.product.seller.userId = :sellerId")
    Optional<Order> findOrderByIdAndSellerId(@Param("orderId") Long orderId, @Param("sellerId") Integer sellerId);
}