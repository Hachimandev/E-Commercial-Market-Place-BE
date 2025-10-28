package com.fit.ecommercialmarketplacebe.service;

import com.fit.ecommercialmarketplacebe.dto.request.CheckoutRequestDto;
import com.fit.ecommercialmarketplacebe.dto.response.*;
import com.fit.ecommercialmarketplacebe.entity.*;
import com.fit.ecommercialmarketplacebe.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final CartService cartService; // Dùng lại service giỏ hàng
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    // ... các repo khác

    // POST /api/orders/checkout
    @Transactional
    public OrderSuccessDto createOrderFromCart(Buyer buyer, CheckoutRequestDto dto) {
        Cart cart = cartService.getOrCreateCart(buyer);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        PaymentMethod paymentMethod = paymentMethodRepository.findById(dto.getPaymentMethodId())
                .filter(pm -> pm.getBuyer().getUserId().equals(buyer.getUserId()))
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found"));

        // 1. Tạo Order
        Order order = new Order();
        order.setBuyer(buyer);
        order.setOrderDate(new Date());
        order.setTotalAmount(cart.getTotalPrice()); // Tạm thời lấy tổng tiền từ cart
        order.setStatus(OrderStatus.PAID); // Giả sử thanh toán thành công ngay
        Order savedOrder = orderRepository.save(order);

        // 2. Chuyển CartItems thành OrderItems
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getProduct().getPrice()); // Lấy giá gốc

                    // TODO: Giảm stock của sản phẩm
                    // Product product = cartItem.getProduct();
                    // product.setStock(product.getStock() - cartItem.getQuantity());
                    // productRepository.save(product);

                    return orderItem;
                })
                .collect(Collectors.toList());
        orderItemRepository.saveAll(orderItems);
        savedOrder.setItems(orderItems);

        // 3. Tạo Payment
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmount(cart.getTotalPrice());
        payment.setDate(new Date());
        payment.setMethod(paymentMethod.getProvider()); // "Visa", "Paypal"
        payment.setStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);

        // 4. XÓA CartItems khỏi giỏ hàng
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartService.recalculateCartTotal(cart); // Sẽ set total = 0

        // 5. Trả về DTO cho SuccessScreen
        return mapOrderToSuccessDto(savedOrder, paymentMethod);
    }

    // Helper: Map PaymentMethod
    private PaymentMethodDto mapPaymentMethodToDto(PaymentMethod pm) {
        return PaymentMethodDto.builder()
                .id(pm.getId().toString())
                .type(pm.getType())
                .brand(pm.getProvider())
                .last4(pm.getLast4())
                .email(pm.getDetails())
                .iconName(pm.getIconName())
                .build();
    }

    // Helper: Map Order -> SuccessDTO
    private OrderSuccessDto mapOrderToSuccessDto(Order order, PaymentMethod paymentMethod) {
        List<CartItemDto> itemDtos = order.getItems().stream()
                .map(item -> CartItemDto.builder()
                        .id(item.getId().toString())
                        .quantity(item.getQuantity())
                        .subtotal(item.getQuantity() * item.getPrice())
                        .productId(item.getProduct().getProductId().toString())
                        .name(item.getProduct().getName())
                        .price(item.getPrice())
                        .imageURL(item.getProduct().getImageURL())
                        .build())
                .collect(Collectors.toList());


        double subtotal = order.getTotalAmount() / 1.1;
        double tax = order.getTotalAmount() - subtotal;

        return OrderSuccessDto.builder()
                .orderId(order.getOrderId().toString())
                .totalAmount(order.getTotalAmount())
                .subtotal(subtotal)
                .tax(tax)
                .paymentMethod(mapPaymentMethodToDto(paymentMethod))
                .itemsPurchased(itemDtos)
                .status("Success")
                .build();
    }


    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }


    public Order getOrderById(Long orderId) {
        Order order = orderRepository.findOrderByOrderId(orderId);
        if (order == null) {
            throw new EntityNotFoundException("Không tìm thấy đơn hàng với id: " + orderId);
        }


        order.getItems().size();


        for (OrderItem item : order.getItems()) {
            if (item.getProduct() != null) {
                item.getProduct().getName();
            }
        }

        return order;
    }

    public void deleteOrderById(Long orderId) {
        Order order = orderRepository.findOrderByOrderId(orderId);
        if (order == null) {
            throw new EntityNotFoundException("Order không tồn tại");
        }
        orderRepository.delete(order);
    }

    public List<Order> getAllOrdersByBuyerUserId(Integer buyerUserId) {
        return orderRepository.findOrderByBuyerUserId(buyerUserId);
    }


}