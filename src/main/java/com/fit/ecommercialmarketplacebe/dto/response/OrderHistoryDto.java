package com.fit.ecommercialmarketplacebe.dto.response;

import com.fit.ecommercialmarketplacebe.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class OrderHistoryDto {
    private String orderId;
    private Date orderDate;
    private double totalAmount;
    private OrderStatus status;
    private int itemCount;
    private List<OrderItemDto> items;
}