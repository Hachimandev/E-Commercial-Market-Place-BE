package com.fit.ecommercialmarketplacebe.service;

import com.fit.ecommercialmarketplacebe.dto.request.AddToCartRequestDto;
import com.fit.ecommercialmarketplacebe.dto.request.UpdateCartItemRequestDto;
import com.fit.ecommercialmarketplacebe.dto.response.CartDto;
import com.fit.ecommercialmarketplacebe.dto.response.CartItemDto;
import com.fit.ecommercialmarketplacebe.entity.*;
import com.fit.ecommercialmarketplacebe.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    // Helper: Lấy giỏ hàng của user, nếu chưa có thì tạo mới
    @Transactional
    public Cart getOrCreateCart(Buyer buyer) {
        return cartRepository.findByBuyer(buyer)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setBuyer(buyer);
                    newCart.setTotalPrice(0);
                    return cartRepository.save(newCart);
                });
    }

    // Helper: Tính toán lại tổng tiền giỏ hàng
    @Transactional
    public void recalculateCartTotal(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
        cart.setTotalPrice(total);
        cartRepository.save(cart);
    }

    // GET /api/cart
    @Transactional(readOnly = true)
    public CartDto getCartDto(Buyer buyer) {
        Cart cart = getOrCreateCart(buyer);
        return mapCartToDto(cart);
    }

    // POST /api/cart/items
    @Transactional
    public CartDto addItemToCart(Buyer buyer, AddToCartRequestDto dto) {
        Cart cart = getOrCreateCart(buyer);
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // TODO: Kiểm tra stock
        if (product.getStock() < dto.getQuantity()) {
            throw new RuntimeException("Not enough stock");
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(dto.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Cập nhật số lượng
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + dto.getQuantity());
            item.setSubtotal(item.getQuantity() * product.getPrice());
            cartItemRepository.save(item);
        } else {
            // Thêm mới
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(dto.getQuantity());
            newItem.setSubtotal(dto.getQuantity() * product.getPrice());
            cart.getItems().add(cartItemRepository.save(newItem));
        }

        recalculateCartTotal(cart);
        return mapCartToDto(cart);
    }

    // PUT /api/cart/items/{itemId}
    @Transactional
    public CartDto updateItemQuantity(Buyer buyer, Long itemId, UpdateCartItemRequestDto dto) {
        Cart cart = getOrCreateCart(buyer);
        CartItem item = cartItemRepository.findById(itemId)
                .filter(ci -> ci.getCart().getCartId().equals(cart.getCartId()))
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found"));

        if (dto.getQuantity() <= 0) {
            // Nếu số lượng <= 0, xóa item
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            // TODO: Kiểm tra stock
            item.setQuantity(dto.getQuantity());
            item.setSubtotal(dto.getQuantity() * item.getProduct().getPrice());
            cartItemRepository.save(item);
        }

        recalculateCartTotal(cart);
        return mapCartToDto(cart);
    }

    // DELETE /api/cart/items/{itemId}
    @Transactional
    public CartDto removeItemFromCart(Buyer buyer, Long itemId) {
        Cart cart = getOrCreateCart(buyer);
        CartItem item = cartItemRepository.findById(itemId)
                .filter(ci -> ci.getCart().getCartId().equals(cart.getCartId()))
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found"));

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        recalculateCartTotal(cart);
        return mapCartToDto(cart);
    }

    // Helper: Map Entity -> DTO
    private CartDto mapCartToDto(Cart cart) {
        List<CartItemDto> itemDtos = cart.getItems().stream()
                .map(item -> CartItemDto.builder()
                        .id(item.getId().toString())
                        .quantity(item.getQuantity())
                        .subtotal(item.getSubtotal())
                        .productId(item.getProduct().getProductId().toString())
                        .name(item.getProduct().getName())
                        .price(item.getProduct().getPrice())
                        .imageURL(item.getProduct().getImageURL())
                        .build())
                .collect(Collectors.toList());

        return CartDto.builder()
                .cartId(cart.getCartId().toString())
                .totalPrice(cart.getTotalPrice())
                .items(itemDtos)
                .build();
    }
}