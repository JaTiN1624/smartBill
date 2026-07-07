package com.smartbill.subscription.controller;

import com.smartbill.common.ApiResponse;
import com.smartbill.security.UserPrincipal;
import com.smartbill.subscription.dto.SubscribeRequest;
import com.smartbill.subscription.entity.Subscription;
import com.smartbill.subscription.entity.SubscriptionPlan;
import com.smartbill.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/plans")
    public ApiResponse<List<SubscriptionPlan>> getPlans() {
        return ApiResponse.ok(subscriptionService.getPlans());
    }

    @PostMapping("/subscribe")
    public ApiResponse<Subscription> subscribe(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody SubscribeRequest request) {
        return ApiResponse.ok("Subscription activated!", subscriptionService.subscribe(principal.getShopId(), request));
    }

    @GetMapping("/history")
    public ApiResponse<List<Subscription>> history(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(subscriptionService.getHistory(principal.getShopId()));
    }
}
