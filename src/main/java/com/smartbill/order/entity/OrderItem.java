package com.smartbill.order.entity;

import com.smartbill.common.BaseEntity;
import com.smartbill.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String productName;

    private BigDecimal unitPrice;

    private BigDecimal taxPercent;

    private Integer quantity;

    private BigDecimal totalPrice;
}
