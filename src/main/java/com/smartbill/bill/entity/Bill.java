package com.smartbill.bill.entity;

import com.smartbill.common.BaseEntity;
import com.smartbill.order.entity.Order;
import com.smartbill.user.entity.Shop;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "bills")
@Getter
@Setter
public class Bill extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String billNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    private String customerName;

    private String customerMobile;

    private String customerAddress;

    private BigDecimal subtotal;

    private BigDecimal taxAmount;

    private BigDecimal discountAmount;

    private BigDecimal totalAmount;

    private String paymentMethod;

    private String paymentStatus;

    private String notes;
}
