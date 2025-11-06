package com.fit.ecommercialmarketplacebe.repository;

import com.fit.ecommercialmarketplacebe.entity.Payment;
import com.fit.ecommercialmarketplacebe.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Lấy doanh thu của Seller theo ngày
    @Query("SELECT COALESCE(SUM(p.amount), 0.0) FROM Payment p " +
            "JOIN p.order o JOIN o.items oi " +
            "WHERE oi.product.seller.userId = :sellerId " +
            "AND p.status = :status AND p.date >= :startDate AND p.date < :endDate")
    Double findTotalRevenueBySellerAndDateRange(
            @Param("sellerId") Integer sellerId,
            @Param("status") PaymentStatus status,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    // Lấy doanh thu của Seller theo tháng
    @Query(value = "SELECT DATE_FORMAT(p.date, '%Y-%m') AS month, SUM(p.amount) AS monthly_revenue " +
            "FROM payment p " +
            "JOIN orders o ON p.order_id = o.order_id " +
            "JOIN order_item oi ON o.order_id = oi.order_id " +
            "JOIN product prod ON oi.product_id = prod.product_id " +
            "WHERE p.status = 'COMPLETED' AND p.date >= :startDate AND prod.seller_id = :sellerId " +
            "GROUP BY month ORDER BY month ASC", nativeQuery = true)
    List<Map<String, Object>> findMonthlyRevenueBySeller(
            @Param("sellerId") Integer sellerId,
            @Param("startDate") Date startDate
    );
}