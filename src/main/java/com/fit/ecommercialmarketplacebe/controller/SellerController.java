package com.fit.ecommercialmarketplacebe.controller;

import com.fit.ecommercialmarketplacebe.entity.Seller;
import com.fit.ecommercialmarketplacebe.entity.User;
import com.fit.ecommercialmarketplacebe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerController {

    private final UserService userService;
    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Seller seller = userService.getSellerFromUserDetails(userDetails);
        return ResponseEntity.ok(seller);
    }
}