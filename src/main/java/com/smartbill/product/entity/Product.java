package com.smartbill.product.entity;

import com.smartbill.common.BaseEntity;
import com.smartbill.user.entity.Shop;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String sku;

    private String description;

    private String unit; // PCS, KG, LTR, BOX, etc.

    @Column(nullable = false)
    private BigDecimal price;

    private BigDecimal purchasePrice;

    private BigDecimal taxPercent = BigDecimal.ZERO;

    private Integer stockQuantity = 0;

    private String hsnCode;

    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;
}
