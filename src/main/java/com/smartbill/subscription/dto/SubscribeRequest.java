package com.smartbill.subscription.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeRequest {

    @NotNull(message = "Plan ID is required")
    private Long planId;

    @NotBlank(message = "Billing cycle is required (MONTHLY or YEARLY)")
    private String billingCycle; // MONTHLY or YEARLY
}
