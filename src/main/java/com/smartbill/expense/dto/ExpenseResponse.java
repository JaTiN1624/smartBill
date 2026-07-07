package com.smartbill.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExpenseResponse {
    private Long id;
    private String title;
    private String category;
    private BigDecimal amount;
    private String notes;
    private LocalDate expenseDate;
    private LocalDateTime createdAt;
}
