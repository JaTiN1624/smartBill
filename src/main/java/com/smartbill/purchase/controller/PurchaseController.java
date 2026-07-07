package com.smartbill.purchase.controller;

import com.smartbill.common.ApiResponse;
import com.smartbill.purchase.dto.PurchaseRequest;
import com.smartbill.purchase.dto.PurchaseResponse;
import com.smartbill.purchase.dto.VendorRequest;
import com.smartbill.purchase.dto.VendorResponse;
import com.smartbill.purchase.service.PurchaseService;
import com.smartbill.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService service;

    @PostMapping("/vendors")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<VendorResponse> createVendor(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody VendorRequest request) {
        return ApiResponse.ok("Vendor created", service.createVendor(principal.getShopId(), request));
    }

    @GetMapping("/vendors")
    public ApiResponse<List<VendorResponse>> getVendors(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(service.getVendors(principal.getShopId()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PurchaseResponse> createPurchase(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody PurchaseRequest request) {
        return ApiResponse.ok("Purchase recorded", service.createPurchase(principal.getShopId(), request));
    }

    @GetMapping
    public ApiResponse<List<PurchaseResponse>> getAllPurchases(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(service.getAllPurchases(principal.getShopId()));
    }
}
