package com.fit.ecommercialmarketplacebe.controller;

import com.fit.ecommercialmarketplacebe.dto.request.CheckoutRequestDto;
import com.fit.ecommercialmarketplacebe.dto.response.OrderSuccessDto;
import com.fit.ecommercialmarketplacebe.entity.Buyer;
import com.fit.ecommercialmarketplacebe.service.OrderService;
import com.fit.ecommercialmarketplacebe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    /**
     * Endpoint xử lý việc thanh toán giỏ hàng và tạo đơn hàng.
     * Được gọi khi người dùng nhấn "Pay Now".
     * Yêu cầu người dùng phải đăng nhập với vai trò BUYER (được kiểm tra bởi SecurityConfig).
     */
    @PostMapping("/checkout")
    public ResponseEntity<OrderSuccessDto> checkout(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CheckoutRequestDto checkoutRequestDto
    ) {

        Buyer buyer = userService.getBuyerFromUserDetails(userDetails);

        OrderSuccessDto orderResult = orderService.createOrderFromCart(buyer, checkoutRequestDto);
        return ResponseEntity.ok(orderResult);
    }
}