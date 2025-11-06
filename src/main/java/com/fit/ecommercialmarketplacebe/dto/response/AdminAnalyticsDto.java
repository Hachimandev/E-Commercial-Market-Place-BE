package com.fit.ecommercialmarketplacebe.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class AdminAnalyticsDto {
    private List<Map<String, Object>> revenueChartData;
    private List<Map<String, Object>> topSellingProducts;
    private List<Map<String, Object>> topActiveUsers;
}