package com.smartbill.subscription.entity;

import com.smartbill.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "subscription_plans")
@Getter
@Setter
public class SubscriptionPlan extends BaseEntity {

    private String planName;

    private BigDecimal monthlyPrice;

    private BigDecimal yearlyPrice;

    private Integer maxProducts;

    private Integer maxOrders;

    private boolean active = true;
}
