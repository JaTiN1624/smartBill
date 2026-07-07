package com.smartbill.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PurchaseResponse {
    private Long id;
    private String vendorName;
    private String productName;
    private Integer quantity;
    private BigDecimal purchasePrice;
    private BigDecimal totalAmount;
    private String notes;
    private LocalDate purchaseDate;
    private LocalDateTime createdAt;
}
