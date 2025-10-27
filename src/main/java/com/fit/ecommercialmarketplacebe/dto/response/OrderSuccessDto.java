package com.fit.ecommercialmarketplacebe.dto.response;
import lombok.Builder;
import lombok.Data;
import java.util.List;
@Data
@Builder
public class OrderSuccessDto {
    private String orderId;
    private double totalAmount;
    private double subtotal;
    private double tax; // (Nếu có)
    private PaymentMethodDto paymentMethod;
    private List<CartItemDto> itemsPurchased; // Danh sách SP đã mua
    private String status; // "Success"
}