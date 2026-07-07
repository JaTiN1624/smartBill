package com.smartbill.quotation.entity;

import com.smartbill.common.BaseEntity;
import com.smartbill.product.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "quotation_items")
@Getter
@Setter
public class QuotationItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id", nullable = false)
    private Quotation quotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxPercent;
    private BigDecimal totalPrice;
}
