package com.smartbill.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CustomerLedgerResponse {
    private Long customerId;
    private String customerName;
    private String mobile;
    private BigDecimal totalBilled;
    private BigDecimal totalPaid;
    private BigDecimal outstanding;
    private int totalOrders;
    private List<LedgerEntry> entries;

    @Getter
    @AllArgsConstructor
    public static class LedgerEntry {
        private Long orderId;
        private String orderNumber;
        private BigDecimal amount;
        private String paymentStatus;
        private String orderStatus;
        private LocalDateTime date;
    }
}
