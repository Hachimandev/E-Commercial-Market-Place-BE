package com.fit.ecommercialmarketplacebe.controller;

import com.fit.ecommercialmarketplacebe.dto.response.PaymentMethodDto;
import com.fit.ecommercialmarketplacebe.entity.Buyer;
import com.fit.ecommercialmarketplacebe.entity.PaymentMethod;
import com.fit.ecommercialmarketplacebe.repository.PaymentMethodRepository;
import com.fit.ecommercialmarketplacebe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentMethodRepository paymentMethodRepository;
    private final UserService userService;

    @GetMapping("/methods")
    public ResponseEntity<List<PaymentMethodDto>> getPaymentMethods(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Buyer buyer = userService.getBuyerFromUserDetails(userDetails);
        List<PaymentMethod> methods = paymentMethodRepository.findByBuyer(buyer);

        List<PaymentMethodDto> dtos = methods.stream()
                .map(pm -> PaymentMethodDto.builder()
                        .id(pm.getId().toString())
                        .type(pm.getType())
                        .brand(pm.getProvider())
                        .last4(pm.getLast4())
                        .email(pm.getDetails())
                        .iconName(pm.getIconName())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}