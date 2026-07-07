package com.smartbill.product.controller;

import com.smartbill.common.ApiResponse;
import com.smartbill.product.dto.CategoryRequest;
import com.smartbill.product.dto.CategoryResponse;
import com.smartbill.product.service.CategoryService;
import com.smartbill.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.ok("Category created", service.createCategory(principal.getShopId(), request));
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAll(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.ok(service.getAllCategories(principal.getShopId()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        service.deleteCategory(principal.getShopId(), id);
        return ApiResponse.ok("Category deleted", null);
    }
}
