package com.fit.ecommercialmarketplacebe.repository;

import com.fit.ecommercialmarketplacebe.entity.OrderStatus;
import com.fit.ecommercialmarketplacebe.entity.Product;
import com.fit.ecommercialmarketplacebe.entity.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByCategoryNameIgnoreCase(String name);
    List<Product> findBySeller(Seller seller);
    long countBySeller(Seller seller);

    @Query("SELECT oi.product FROM OrderItem oi " +
            "JOIN oi.order o " +
            "WHERE o.status = :status " +
            "GROUP BY oi.product.productId " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<Product> findBestSellingProducts(
            @Param("status") OrderStatus status,
            Pageable pageable
    );

    @Query(value = "SELECT p.product_id as productId, p.name as name, SUM(oi.quantity) as totalSold " +
            "FROM order_item oi " +
            "JOIN orders o ON oi.order_id = o.order_id " +
            "JOIN product p ON oi.product_id = p.product_id " +
            "WHERE o.status = :status AND p.seller_id = :sellerId " +
            "GROUP BY p.product_id, p.name " +
            "ORDER BY totalSold DESC", nativeQuery = true)
    List<Map<String, Object>> findBestSellingProductsBySeller(
            @Param("sellerId") Integer sellerId,
            @Param("status") String status,
            Pageable pageable
    );

    @Query(value = "SELECT p.product_id as id, p.name as name, SUM(oi.quantity) as value1, SUM(oi.quantity * oi.price) as value2 " +
            "FROM order_item oi " +
            "JOIN orders o ON oi.order_id = o.order_id " +
            "JOIN product p ON oi.product_id = p.product_id " +
            "WHERE o.status = 'DELIVERED' AND p.seller_id = :sellerId " +
            "GROUP BY p.product_id, p.name " +
            "ORDER BY value2 DESC", nativeQuery = true)
    List<Map<String, Object>> findTopSellingProductsWithRevenueBySeller(
            @Param("sellerId") Integer sellerId, Pageable pageable);
}