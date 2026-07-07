package com.smartbill.product.controller;

import com.smartbill.common.ApiResponse;
import com.smartbill.product.dto.ProductRequest;
import com.smartbill.product.dto.ProductResponse;
import com.smartbill.product.service.ProductService;
import com.smartbill.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductResponse> createProduct(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ProductRequest request) {
        return ApiResponse.ok("Product created", service.createProduct(principal.getShopId(), request));
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProducts(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) String search) {
        return ApiResponse.ok(service.getAllProducts(principal.getShopId(), search));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        return ApiResponse.ok(service.getProductById(principal.getShopId(), id));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ApiResponse.ok("Product updated", service.updateProduct(principal.getShopId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        service.deleteProduct(principal.getShopId(), id);
        return ApiResponse.ok("Product deleted", null);
    }

    @GetMapping("/low-stock")
    public ApiResponse<List<ProductResponse>> getLowStock(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "5") int threshold) {
        return ApiResponse.ok(service.getLowStock(principal.getShopId(), threshold));
    }
}
