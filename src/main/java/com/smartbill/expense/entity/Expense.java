package com.smartbill.expense.entity;

import com.smartbill.common.BaseEntity;
import com.smartbill.user.entity.Shop;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Getter
@Setter
public class Expense extends BaseEntity {

    @Column(nullable = false)
    private String title;

    private String category;

    @Column(nullable = false)
    private BigDecimal amount;

    private String notes;

    private LocalDate expenseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;
}
