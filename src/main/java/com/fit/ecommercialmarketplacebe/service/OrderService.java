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
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    // POST /api/orders/checkout
    @Transactional
    public OrderSuccessDto createOrderFromCart(Buyer buyer, CheckoutRequestDto dto) {
        Cart cart = cartService.getOrCreateCart(buyer);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống");
        }

        PaymentMethod paymentMethod = paymentMethodRepository.findById(dto.getPaymentMethodId())
                .filter(pm -> pm.getBuyer().getUserId().equals(buyer.getUserId()))
                .orElseThrow(() -> new EntityNotFoundException("Payment method không tìm thấy"));

        Order order = new Order();
        order.setBuyer(buyer);
        order.setOrderDate(new Date());
        order.setTotalAmount(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getProduct().getPrice());

                    // TODO: Giảm stock của sản phẩm
                    // Product product = cartItem.getProduct();
                    // product.setStock(product.getStock() - cartItem.getQuantity());
                    // productRepository.save(product);

                    return orderItem;
                })
                .collect(Collectors.toList());
        orderItemRepository.saveAll(orderItems);
        savedOrder.setItems(orderItems);

        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmount(cart.getTotalPrice());
        payment.setDate(new Date());
        payment.setMethod(paymentMethod.getProvider());
        payment.setStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);

        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartService.recalculateCartTotal(cart);

        return mapOrderToSuccessDto(savedOrder, paymentMethod);
    }

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

    @Transactional(readOnly = true)
    public List<OrderHistoryDto> getOrderHistory(Buyer buyer) {
        List<Order> orders = orderRepository.findByBuyerOrderByOrderDateDesc(buyer);

        return orders.stream()
                .map(this::mapOrderToHistoryDto)
                .collect(Collectors.toList());
    }

    private OrderHistoryDto mapOrderToHistoryDto(Order order) {
        if (order.getItems() == null) {
            throw new EntityNotFoundException("Danh sách hóa đơn rỗng!");
        }

        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(item -> OrderItemDto.builder()
                        .productId(item.getProduct().getProductId().toString())
                        .productName(item.getProduct().getName())
                        .productImageURL(item.getProduct().getImageURL())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());
        int totalItems = order.getItems().stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
        return OrderHistoryDto.builder()
                .orderId(order.getOrderId().toString())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .itemCount(totalItems)
                .items(itemDtos)
                .build();
    }

    @Transactional(readOnly = true)
    public OrderDetailDto getOrderDetail(Long orderId, Buyer buyer) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Hóa đơn không tìm thấy với mã: " + orderId));
        if (!order.getBuyer().getUserId().equals(buyer.getUserId())) {
            throw new EntityNotFoundException("Không tim thấy hóa đơn cho user");
        }

        return mapOrderToDetailDto(order);
    }

    private OrderDetailDto mapOrderToDetailDto(Order order) {
        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(item -> OrderItemDto.builder()
                        .productId(item.getProduct().getProductId().toString())
                        .productName(item.getProduct().getName())
                        .productImageURL(item.getProduct().getImageURL())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());

        String paymentMethodDetail = order.getPayment() != null ? order.getPayment().getMethod() : "N/A";
        String paymentStatusDetail = order.getPayment() != null ? order.getPayment().getStatus().name() : "N/A";


        return OrderDetailDto.builder()
                .orderId(order.getOrderId().toString())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .items(itemDtos)
                .buyerName(order.getBuyer().getFullName())
                .shippingAddress(order.getBuyer().getAddress())
                .buyerPhone(order.getBuyer().getPhone())
                .paymentMethod(paymentMethodDetail)
                .paymentStatus(paymentStatusDetail)
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