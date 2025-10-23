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
        if (userRepo.existsByUsername(req.getName())) return "Tên đăng nhập đã tồn tại";
        User u = User.builder()
                .username(req.getName())
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

