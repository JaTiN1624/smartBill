package com.smartbill.quotation.dto;

import com.smartbill.quotation.entity.QuotationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class QuotationResponse {
    private Long id;
    private String quotationNumber;
    private String customerName;
    private String customerMobile;
    private List<QuotationItemDto> items;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String notes;
    private LocalDate validUntil;
    private QuotationStatus status;
    private LocalDateTime createdAt;

    @Getter
    @AllArgsConstructor
    public static class QuotationItemDto {
        private Long id;
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal taxPercent;
        private BigDecimal totalPrice;
    }
}
