package com.fit.ecommercialmarketplacebe.repository;

import com.fit.ecommercialmarketplacebe.entity.Role;
import com.fit.ecommercialmarketplacebe.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    long countByRole(Role role);
    List<User> findByRole(Role role);
    @Query(value = "SELECT u.user_id as id, u.full_name as name, COUNT(DISTINCT o.order_id) as value1, SUM(p.amount) as value2 " +
            "FROM users u " +
            "JOIN buyer b ON u.user_id = b.user_id " +
            "JOIN orders o ON b.user_id = o.buyer_id " +
            "JOIN payment p ON o.order_id = p.order_id " +
            "WHERE o.status = 'DELIVERED' AND p.status = 'COMPLETED' " +
            "AND o.order_id IN (SELECT DISTINCT oi.order_id FROM order_item oi JOIN product pr ON oi.product_id = pr.product_id WHERE pr.seller_id = :sellerId) " +
            "GROUP BY u.user_id, u.full_name " +
            "ORDER BY value2 DESC", nativeQuery = true)
    List<Map<String, Object>> findTopActiveUsersBySeller(
            @Param("sellerId") Integer sellerId, Pageable pageable);
}
