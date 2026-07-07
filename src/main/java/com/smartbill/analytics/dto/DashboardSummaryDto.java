package com.smartbill.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardSummaryDto {
    private BigDecimal todayRevenue;
    private Long todayOrders;
    private BigDecimal monthRevenue;
    private Long monthOrders;
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private Long totalProducts;
    private Long totalCustomers;
    private List<TopProductDto> topProducts;
    private List<MonthlySalesDto> monthlySales;
}
