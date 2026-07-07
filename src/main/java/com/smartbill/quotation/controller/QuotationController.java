package com.smartbill.quotation.controller;

import com.smartbill.common.ApiResponse;
import com.smartbill.quotation.dto.QuotationRequest;
import com.smartbill.quotation.dto.QuotationResponse;
import com.smartbill.quotation.entity.QuotationStatus;
import com.smartbill.quotation.service.QuotationService;
import com.smartbill.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<QuotationResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody QuotationRequest request) {
        return ApiResponse.ok("Quotation created", service.create(principal.getShopId(), request));
    }

    @GetMapping
    public ApiResponse<List<QuotationResponse>> getAll(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(service.getAll(principal.getShopId()));
    }

    @GetMapping("/{id}")
    public ApiResponse<QuotationResponse> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        return ApiResponse.ok(service.getById(principal.getShopId(), id));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<QuotationResponse> updateStatus(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @RequestParam QuotationStatus status) {
        return ApiResponse.ok("Status updated", service.updateStatus(principal.getShopId(), id, status));
    }
}
