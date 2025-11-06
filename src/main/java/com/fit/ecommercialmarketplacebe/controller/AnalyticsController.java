package com.fit.ecommercialmarketplacebe.controller;

import com.fit.ecommercialmarketplacebe.dto.response.AdminDashboardStatsDto;
import com.fit.ecommercialmarketplacebe.dto.response.AdminAnalyticsDto;
import com.fit.ecommercialmarketplacebe.entity.Seller;
import com.fit.ecommercialmarketplacebe.service.AnalyticsService;
import com.fit.ecommercialmarketplacebe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final UserService userService;

    private Seller getSeller(UserDetails userDetails) {
        return userService.getSellerFromUserDetails(userDetails);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardStatsDto> getDashboardStats(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(analyticsService.getDashboardStats(getSeller(userDetails)));
    }

    @GetMapping("/reports")
    public ResponseEntity<AdminAnalyticsDto> getAnalyticsReports(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(analyticsService.getAnalyticsReports(getSeller(userDetails)));
    }
}