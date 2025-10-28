package com.fit.ecommercialmarketplacebe.controller;


import com.fit.ecommercialmarketplacebe.config.JwtService;
import com.fit.ecommercialmarketplacebe.dto.AuthRequest;
import com.fit.ecommercialmarketplacebe.dto.AuthResponse;
import com.fit.ecommercialmarketplacebe.dto.RegisterRequest;
import com.fit.ecommercialmarketplacebe.entity.Buyer;
import com.fit.ecommercialmarketplacebe.entity.Cart;
import com.fit.ecommercialmarketplacebe.entity.Role;
import com.fit.ecommercialmarketplacebe.entity.User;
import com.fit.ecommercialmarketplacebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private AuthenticationManager authManager;
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepo.existsByUsername(req.getName()))
            return ResponseEntity.badRequest().body(Map.of("error", "Tên đăng nhập đã tồn tại"));

        Buyer buyer = new Buyer();
        buyer.setUsername(req.getName());
        buyer.setPassword(passwordEncoder.encode(req.getPassword()));
        buyer.setFullName(req.getFullName());
        buyer.setPhone(req.getPhone());
        buyer.setAddress(req.getAddress());
        buyer.setRole(Role.BUYER);

        Cart cart = new Cart();
        cart.setBuyer(buyer);
        buyer.setCart(cart);

        userRepo.save(buyer);

        return ResponseEntity.ok(buyer);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        User user = userRepo.findByUsername(req.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        long expiresAt = System.currentTimeMillis() + 86400000L;
        return new AuthResponse(
                token,
                user.getUsername(),
                user.getRole().name(),
                user.getFullName(),
                user.getPhone(),
                user.getAddress(),
                expiresAt
                );
    }


}

