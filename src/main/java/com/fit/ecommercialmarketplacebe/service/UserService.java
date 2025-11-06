package com.fit.ecommercialmarketplacebe.service;

import com.fit.ecommercialmarketplacebe.dto.response.UserDto;
import com.fit.ecommercialmarketplacebe.entity.Buyer;
import com.fit.ecommercialmarketplacebe.entity.Role;
import com.fit.ecommercialmarketplacebe.entity.Seller;
import com.fit.ecommercialmarketplacebe.entity.User;
import com.fit.ecommercialmarketplacebe.repository.BuyerRepository;
import com.fit.ecommercialmarketplacebe.repository.SellerRepository;
import com.fit.ecommercialmarketplacebe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException; // <-- Import
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;

    @Transactional(readOnly = true)
    public User getUserFromUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Transactional(readOnly = true)
    public Buyer getBuyerFromUserDetails(UserDetails userDetails) {
        if (userDetails == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String username = userDetails.getUsername();
        User user = getUserFromUsername(username);

        if (user.getRole() != Role.BUYER) {
            throw new AccessDeniedException("User is not a Buyer");
        }

        return buyerRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Buyer profile not found for user: " + username));
    }

    public Seller getSellerFromUserDetails(UserDetails userDetails) {
        if (userDetails == null) {
            throw new AccessDeniedException("User is not authenticated");
        }
        String username = userDetails.getUsername();
        User user = getUserFromUsername(username);

        if (user.getRole() != Role.SELLER) {
            throw new AccessDeniedException("User is not a Seller");
        }
        return sellerRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Seller profile not found for user: " + username));
    }

    public List<UserDto> findUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::mapUserToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto toggleUserStatus(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return mapUserToDto(user);
    }

    private UserDto mapUserToDto(User user) {
        return UserDto.builder()
                .id(user.getUserId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .avatarURL(user.getAvatarURL())
                .role(user.getRole().name())
                .status("Active")
                .build();
    }
}