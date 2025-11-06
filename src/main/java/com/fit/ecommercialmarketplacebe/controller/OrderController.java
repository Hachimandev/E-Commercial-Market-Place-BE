package com.fit.ecommercialmarketplacebe.controller;

import com.fit.ecommercialmarketplacebe.dto.request.CheckoutRequestDto;
import com.fit.ecommercialmarketplacebe.dto.response.OrderDetailDto;
import com.fit.ecommercialmarketplacebe.dto.response.OrderHistoryDto;
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

    @PostMapping("/checkout")
    public ResponseEntity<OrderSuccessDto> checkout(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CheckoutRequestDto checkoutRequestDto
    ) {

        Buyer buyer = userService.getBuyerFromUserDetails(userDetails);

        OrderSuccessDto orderResult = orderService.createOrderFromCart(buyer, checkoutRequestDto);
        return ResponseEntity.ok(orderResult);
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderHistoryDto>> getOrderHistory(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Buyer buyer = userService.getBuyerFromUserDetails(userDetails);
        List<OrderHistoryDto> history = orderService.getOrderHistory(buyer);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailDto> getOrderDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long orderId
    ) {
        Buyer buyer = userService.getBuyerFromUserDetails(userDetails);
        OrderDetailDto orderDetail = orderService.getOrderDetail(orderId, buyer);
        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersForAdmin() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }


    @GetMapping("/admin/{id}")
    public ResponseEntity<Order> getOrderByIdForAdmin(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteOrderByIdForAdmin(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }

}