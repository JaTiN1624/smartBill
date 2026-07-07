package com.smartbill.bill.dto;

import com.smartbill.order.dto.OrderItemResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class BillResponse {
    private Long id;
    private String billNumber;
    private String orderNumber;
    private Long orderId;
    private String shopName;
    private String shopAddress;
    private String shopGst;
    private String currency;
    private String customerName;
    private String customerMobile;
    private String customerAddress;
    private List<OrderItemResponse> items;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String paymentStatus;
    private String notes;
    private LocalDateTime billDate;
}
