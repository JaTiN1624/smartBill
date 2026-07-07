package com.smartbill.analytics.controller;

import com.smartbill.analytics.dto.DashboardSummaryDto;
import com.smartbill.analytics.dto.MonthlySalesDto;
import com.smartbill.analytics.dto.TopProductDto;
import com.smartbill.analytics.service.AnalyticsService;
import com.smartbill.common.ApiResponse;
import com.smartbill.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ApiResponse<DashboardSummaryDto> getDashboard(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(analyticsService.getDashboard(principal.getShopId()));
    }

    @GetMapping("/top-products")
    public ApiResponse<List<TopProductDto>> getTopProducts(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "10") int limit) {
        return ApiResponse.ok(analyticsService.getTopProducts(principal.getShopId(), from, to, limit));
    }

    @GetMapping("/monthly-sales")
    public ApiResponse<List<MonthlySalesDto>> getMonthlySales(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "12") int months) {
        return ApiResponse.ok(analyticsService.getMonthlySales(principal.getShopId(), months));
    }
}
