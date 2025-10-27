package com.fit.ecommercialmarketplacebe.dto.request;
import lombok.Data;
@Data
public class CheckoutRequestDto {
    private Long paymentMethodId;
    private String voucherCode; // (Optional)
}