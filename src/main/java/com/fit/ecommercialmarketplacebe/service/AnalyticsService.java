package com.fit.ecommercialmarketplacebe.service;

import com.fit.ecommercialmarketplacebe.dto.response.AdminDashboardStatsDto;
import com.fit.ecommercialmarketplacebe.dto.response.AdminAnalyticsDto;
import com.fit.ecommercialmarketplacebe.entity.*;
import com.fit.ecommercialmarketplacebe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public AdminDashboardStatsDto getDashboardStats(Seller seller) {
        long totalProducts = productRepository.countBySeller(seller);
        long totalUsers = userRepository.countByRole(Role.BUYER);
        long totalOrders = orderRepository.countBySeller(seller.getUserId());

        LocalDate today = LocalDate.now();
        Date startDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        double todayRevenue = paymentRepository.findTotalRevenueBySellerAndDateRange(
                seller.getUserId(), PaymentStatus.COMPLETED, startDate, endDate
        );

        List<Map<String, Object>> topSelling = getTopSelling(seller, 5);
        List<Map<String, Object>> revenueChart = getRevenueChart(seller, 6);

        return AdminDashboardStatsDto.builder()
                .totalProducts(totalProducts)
                .totalUsers(totalUsers)
                .totalOrders(totalOrders)
                .todayRevenue(todayRevenue)
                .topSellingProducts(topSelling)
                .revenueChartData(revenueChart)
                .build();
    }

    public AdminAnalyticsDto getAnalyticsReports(Seller seller) {
        List<Map<String, Object>> topSelling = productRepository.findTopSellingProductsWithRevenueBySeller(
                seller.getUserId(), PageRequest.of(0, 3));

        List<Map<String, Object>> revenueChart = getRevenueChart(seller, 3);

        List<Map<String, Object>> topUsers = userRepository.findTopActiveUsersBySeller(
                seller.getUserId(), PageRequest.of(0, 3));

        return AdminAnalyticsDto.builder()
                .topSellingProducts(topSelling)
                .revenueChartData(revenueChart)
                .topActiveUsers(topUsers)
                .build();
    }

    private List<Map<String, Object>> getTopSelling(Seller seller, int limit) {
        return productRepository.findBestSellingProductsBySeller(
                seller.getUserId(), OrderStatus.DELIVERED.name(), PageRequest.of(0, limit));
    }

    private List<Map<String, Object>> getRevenueChart(Seller seller, int months) {
        LocalDate startDate = LocalDate.now().minusMonths(months);
        Date sqlStartDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return paymentRepository.findMonthlyRevenueBySeller(seller.getUserId(), sqlStartDate);
    }
}