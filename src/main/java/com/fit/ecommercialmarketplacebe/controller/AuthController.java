package com.fit.ecommercialmarketplacebe.controller;


import com.fit.ecommercialmarketplacebe.config.JwtService;
import com.fit.ecommercialmarketplacebe.dto.AuthRequest;
import com.fit.ecommercialmarketplacebe.dto.AuthResponse;
import com.fit.ecommercialmarketplacebe.dto.RegisterRequest;
import com.fit.ecommercialmarketplacebe.entity.Role;
import com.fit.ecommercialmarketplacebe.entity.User;
import com.fit.ecommercialmarketplacebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private AuthenticationManager authManager;
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest req) {
        if (userRepo.existsByName(req.getName())) return "Tên đăng nhập đã tồn tại";
        User u = User.builder()
                .name(req.getName())
                .password(passwordEncoder.encode(req.getPassword()))
                .phone(req.getPhone())
                .address(req.getAddress())
                .role(Role.valueOf(req.getRole()))
                .build();
        userRepo.save(u);
        return "Đăng ký thành công";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getName(), req.getPassword()));
        User user = userRepo.findByName(req.getName()).orElseThrow();
        String token = jwtService.generateToken(user.getName(), user.getRole().name());
        long expiresAt = System.currentTimeMillis() + 86400000L;
        return new AuthResponse(token, user.getName(), user.getRole().name(), expiresAt);
    }
}

