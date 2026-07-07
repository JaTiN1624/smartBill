package com.smartbill.bill.controller;

import com.smartbill.bill.dto.BillResponse;
import com.smartbill.bill.service.BillService;
import com.smartbill.common.ApiResponse;
import com.smartbill.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping("/generate/{orderId}")
    public ApiResponse<BillResponse> generate(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long orderId) {
        return ApiResponse.ok("Bill generated", billService.generateBill(principal.getShopId(), orderId));
    }

    @GetMapping
    public ApiResponse<List<BillResponse>> getAllBills(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(billService.getAllBills(principal.getShopId()));
    }

    @GetMapping("/{id}")
    public ApiResponse<BillResponse> getBill(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        return ApiResponse.ok(billService.getBill(principal.getShopId(), id));
    }
}
