package com.smartbill.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class MonthlySalesDto {
    private int year;
    private int month;
    private String monthName;
    private Long totalOrders;
    private BigDecimal totalRevenue;
}
