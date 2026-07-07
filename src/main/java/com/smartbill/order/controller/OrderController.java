package com.smartbill.order.controller;

import com.smartbill.common.ApiResponse;
import com.smartbill.order.dto.OrderRequest;
import com.smartbill.order.dto.OrderResponse;
import com.smartbill.order.service.OrderService;
import com.smartbill.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> createOrder(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody OrderRequest request) {
        return ApiResponse.ok("Order created successfully", orderService.createOrder(principal.getShopId(), request));
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> getOrders(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String status) {
        if (from != null || to != null || status != null) {
            return ApiResponse.ok(orderService.getOrdersFiltered(principal.getShopId(), from, to, status));
        }
        return ApiResponse.ok(orderService.getOrders(principal.getShopId()));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrder(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        return ApiResponse.ok(orderService.getOrderById(principal.getShopId(), id));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<OrderResponse> updateStatus(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ApiResponse.ok("Status updated",
                orderService.updateStatus(principal.getShopId(), id,
                        body.get("orderStatus"), body.get("paymentStatus")));
    }
}
