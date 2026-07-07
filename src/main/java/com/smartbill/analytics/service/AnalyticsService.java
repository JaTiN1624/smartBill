package com.smartbill.analytics.service;

import com.smartbill.analytics.dto.DashboardSummaryDto;
import com.smartbill.analytics.dto.MonthlySalesDto;
import com.smartbill.analytics.dto.TopProductDto;
import com.smartbill.customer.repository.CustomerRepository;
import com.smartbill.order.repository.OrderItemRepository;
import com.smartbill.order.repository.OrderRepository;
import com.smartbill.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public DashboardSummaryDto getDashboard(Long shopId) {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(23, 59, 59);
        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime monthEnd = LocalDate.now().atTime(23, 59, 59);

        BigDecimal todayRevenue = orderRepository.sumRevenueByShopAndDateRange(shopId, todayStart, todayEnd);
        Long todayOrders = orderRepository.countByShopAndDateRange(shopId, todayStart, todayEnd);
        BigDecimal monthRevenue = orderRepository.sumRevenueByShopAndDateRange(shopId, monthStart, monthEnd);
        Long monthOrders = orderRepository.countByShopAndDateRange(shopId, monthStart, monthEnd);
        BigDecimal totalRevenue = orderRepository.sumRevenueByShopAndDateRange(shopId,
                LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now());
        Long totalOrders = orderRepository.countByShopAndDateRange(shopId,
                LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.now());

        Long totalProducts = (long) productRepository.findByShopIdAndActiveTrue(shopId).size();
        Long totalCustomers = (long) customerRepository.findByShopId(shopId).size();

        List<TopProductDto> topProducts = getTopProducts(shopId, monthStart, monthEnd, 10);
        List<MonthlySalesDto> monthlySales = getMonthlySales(shopId, 12);

        return new DashboardSummaryDto(
                todayRevenue, todayOrders, monthRevenue, monthOrders,
                totalRevenue, totalOrders, totalProducts, totalCustomers,
                topProducts, monthlySales
        );
    }

    public List<TopProductDto> getTopProducts(Long shopId, LocalDateTime from, LocalDateTime to, int limit) {
        List<Object[]> rows = orderItemRepository.findTopProducts(shopId, from, to);
        List<TopProductDto> result = new ArrayList<>();
        int count = 0;
        for (Object[] row : rows) {
            if (count >= limit) break;
            result.add(new TopProductDto(
                    ((Number) row[0]).longValue(),
                    (String) row[1],
                    ((Number) row[2]).longValue(),
                    (BigDecimal) row[3]
            ));
            count++;
        }
        return result;
    }

    public List<MonthlySalesDto> getMonthlySales(Long shopId, int months) {
        List<MonthlySalesDto> result = new ArrayList<>();
        for (int i = months - 1; i >= 0; i--) {
            YearMonth ym = YearMonth.now().minusMonths(i);
            LocalDateTime from = ym.atDay(1).atStartOfDay();
            LocalDateTime to = ym.atEndOfMonth().atTime(23, 59, 59);

            BigDecimal revenue = orderRepository.sumRevenueByShopAndDateRange(shopId, from, to);
            Long orders = orderRepository.countByShopAndDateRange(shopId, from, to);
            String monthName = ym.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    + " " + ym.getYear();

            result.add(new MonthlySalesDto(ym.getYear(), ym.getMonthValue(), monthName,
                    orders, revenue));
        }
        return result;
    }
}
