package com.smartbill.purchase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PurchaseRequest {
    private Long vendorId;
    @NotNull
    private Long productId;
    @NotNull
    @Min(1)
    private Integer quantity;
    @NotNull
    private BigDecimal purchasePrice;
    private String notes;
    private LocalDate purchaseDate;
}
