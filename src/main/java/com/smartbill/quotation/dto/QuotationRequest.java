package com.smartbill.quotation.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class QuotationRequest {
    private Long customerId;
    @NotEmpty
    private List<QuotationItemRequest> items;
    private BigDecimal discountAmount;
    private String notes;
    private LocalDate validUntil;
}
