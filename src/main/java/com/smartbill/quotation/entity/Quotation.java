package com.smartbill.quotation.entity;

import com.smartbill.common.BaseEntity;
import com.smartbill.customer.entity.Customer;
import com.smartbill.user.entity.Shop;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "quotations")
@Getter
@Setter
public class Quotation extends BaseEntity {

    private String quotationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuotationItem> items;

    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;

    private String notes;
    private LocalDate validUntil;

    @Enumerated(EnumType.STRING)
    private QuotationStatus status = QuotationStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;
}
