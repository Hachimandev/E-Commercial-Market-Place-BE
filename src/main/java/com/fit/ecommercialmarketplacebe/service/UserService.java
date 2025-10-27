package com.fit.ecommercialmarketplacebe.service;

import com.fit.ecommercialmarketplacebe.entity.Buyer;
import com.fit.ecommercialmarketplacebe.entity.Role;
import com.fit.ecommercialmarketplacebe.entity.User;
import com.fit.ecommercialmarketplacebe.repository.BuyerRepository;
import com.fit.ecommercialmarketplacebe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException; // <-- Import
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // (Dùng @RequiredArgsConstructor thay cho @Autowired)
public class UserService {

    private final UserRepository userRepository;
    private final BuyerRepository buyerRepository;

    /**
     * Lấy thông tin User từ username.
     * (Hàm này có thể dùng chung cho cả UserDetailsServiceImpl nếu bạn refactor)
     */
    @Transactional(readOnly = true)
    public User getUserFromUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    /**
     * Hàm helper chính mà CartController và OrderController cần dùng.
     * Lấy thực thể Buyer từ thông tin đăng nhập UserDetails.
     */
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
}