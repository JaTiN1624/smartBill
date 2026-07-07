package com.smartbill.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String sku;
    private String description;
    private String unit;
    private BigDecimal price;
    private BigDecimal purchasePrice;
    private BigDecimal taxPercent;
    private Integer stockQuantity;
    private String categoryName;
    private String hsnCode;
}
