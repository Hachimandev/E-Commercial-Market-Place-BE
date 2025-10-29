package com.fit.ecommercialmarketplacebe.dto.response;

import com.fit.ecommercialmarketplacebe.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class OrderDetailDto {
    private String orderId;
    private Date orderDate;
    private double totalAmount;
    private OrderStatus status;
    private List<OrderItemDto> items;

    private String buyerName;
    private String shippingAddress;
    private String buyerPhone;

    private String paymentMethod;
    private String paymentStatus;
}