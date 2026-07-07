package com.smartbill.expense.controller;

import com.smartbill.common.ApiResponse;
import com.smartbill.expense.dto.ExpenseRequest;
import com.smartbill.expense.dto.ExpenseResponse;
import com.smartbill.expense.service.ExpenseService;
import com.smartbill.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ExpenseResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ExpenseRequest request) {
        return ApiResponse.ok("Expense added", service.create(principal.getShopId(), request));
    }

    @GetMapping
    public ApiResponse<List<ExpenseResponse>> getAll(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(service.getAll(principal.getShopId()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        service.delete(principal.getShopId(), id);
        return ApiResponse.ok("Expense deleted", null);
    }
}
