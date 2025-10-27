package com.fit.ecommercialmarketplacebe.controller;

import com.fit.ecommercialmarketplacebe.dto.request.AddToCartRequestDto;
import com.fit.ecommercialmarketplacebe.dto.request.UpdateCartItemRequestDto;
import com.fit.ecommercialmarketplacebe.dto.response.CartDto;
import com.fit.ecommercialmarketplacebe.entity.Buyer; // Giả sử bạn có entity Buyer
import com.fit.ecommercialmarketplacebe.service.CartService;
import com.fit.ecommercialmarketplacebe.service.UserService; // Service để lấy user
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    private Buyer getBuyer(UserDetails userDetails) {
        return userService.getBuyerFromUserDetails(userDetails);
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Buyer buyer = getBuyer(userDetails);
        return ResponseEntity.ok(cartService.getCartDto(buyer));
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddToCartRequestDto dto
    ) {
        Buyer buyer = getBuyer(userDetails);
        return ResponseEntity.ok(cartService.addItemToCart(buyer, dto));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartDto> updateItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId,
            @RequestBody UpdateCartItemRequestDto dto
    ) {
        Buyer buyer = getBuyer(userDetails);
        return ResponseEntity.ok(cartService.updateItemQuantity(buyer, itemId, dto));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartDto> removeItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId
    ) {
        Buyer buyer = getBuyer(userDetails);
        return ResponseEntity.ok(cartService.removeItemFromCart(buyer, itemId));
    }
}