package com.fit.ecommercialmarketplacebe.controller;

import com.fit.ecommercialmarketplacebe.dto.response.UserDto;
import com.fit.ecommercialmarketplacebe.entity.Role;
import com.fit.ecommercialmarketplacebe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable String role) {
        Role r = Role.valueOf(role.toUpperCase());
        return ResponseEntity.ok(userService.findUsersByRole(r));
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<UserDto> toggleUserStatus(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.toggleUserStatus(id));
    }
}