package com.smartbill.user.controller;

import com.smartbill.common.ApiResponse;
import com.smartbill.security.UserPrincipal;
import com.smartbill.user.dto.ShopRequest;
import com.smartbill.user.dto.ShopResponse;
import com.smartbill.user.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService service;

    @GetMapping
    public ApiResponse<ShopResponse> getShop(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(service.getShop(principal.getShopId(), principal.getUserId()));
    }

    @PutMapping
    public ApiResponse<ShopResponse> updateShop(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ShopRequest request) {
        return ApiResponse.ok("Shop updated", service.updateShop(principal.getShopId(), principal.getUserId(), request));
    }
}
