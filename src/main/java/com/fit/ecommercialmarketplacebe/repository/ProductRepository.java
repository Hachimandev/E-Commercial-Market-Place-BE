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

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByCategoryNameIgnoreCase(String name);
    List<Product> findBySeller(Seller seller);

    @Query("SELECT oi.product FROM OrderItem oi " +
            "JOIN oi.order o " +
            "WHERE o.status = :status " +
            "GROUP BY oi.product.productId " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<Product> findBestSellingProducts(
            @Param("status") OrderStatus status,
            Pageable pageable
    );
}