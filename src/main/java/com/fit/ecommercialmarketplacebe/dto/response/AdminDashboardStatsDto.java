package com.fit.ecommercialmarketplacebe.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class AdminDashboardStatsDto {
    private long totalProducts;
    private long totalUsers;
    private long totalOrders;
    private double todayRevenue;

    private List<Map<String, Object>> revenueChartData;
    private List<Map<String, Object>> topSellingProducts;
}