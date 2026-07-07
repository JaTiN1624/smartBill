package com.smartbill.customer.controller;

import com.smartbill.common.ApiResponse;
import com.smartbill.customer.dto.CustomerLedgerResponse;
import com.smartbill.customer.dto.CustomerRequest;
import com.smartbill.customer.dto.CustomerResponse;
import com.smartbill.customer.service.CustomerService;
import com.smartbill.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CustomerResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CustomerRequest request) {
        return ApiResponse.ok("Customer created", customerService.create(principal.getShopId(), request));
    }

    @GetMapping
    public ApiResponse<List<CustomerResponse>> getAll(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String search) {
        return ApiResponse.ok(customerService.getAll(principal.getShopId(), search));
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomerResponse> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        return ApiResponse.ok(customerService.getById(principal.getShopId(), id));
    }

    @PutMapping("/{id}")
    public ApiResponse<CustomerResponse> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {
        return ApiResponse.ok("Customer updated", customerService.update(principal.getShopId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        customerService.delete(principal.getShopId(), id);
        return ApiResponse.ok("Customer deleted", null);
    }

    @GetMapping("/{id}/ledger")
    public ApiResponse<CustomerLedgerResponse> getLedger(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        return ApiResponse.ok(customerService.getLedger(principal.getShopId(), id));
    }
}
