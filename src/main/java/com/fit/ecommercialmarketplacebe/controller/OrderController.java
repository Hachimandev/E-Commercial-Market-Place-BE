package com.fit.ecommercialmarketplacebe.controller;

import com.fit.ecommercialmarketplacebe.dto.request.CheckoutRequestDto;
import com.fit.ecommercialmarketplacebe.dto.response.OrderSuccessDto;
import com.fit.ecommercialmarketplacebe.entity.Buyer;
import com.fit.ecommercialmarketplacebe.entity.Order;
import com.fit.ecommercialmarketplacebe.service.OrderService;
import com.fit.ecommercialmarketplacebe.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/buyer")
    public ResponseEntity<List<Order>> getOrdersForBuyer(
            @AuthenticationPrincipal UserDetails userDetails) {
        var buyer = userService.getBuyerFromUserDetails(userDetails);
        return ResponseEntity.ok(orderService.getAllOrdersByBuyerUserId(buyer.getUserId()));
    }


}