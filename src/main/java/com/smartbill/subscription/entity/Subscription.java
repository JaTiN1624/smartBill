package com.smartbill.subscription.entity;

import com.smartbill.common.BaseEntity;
import com.smartbill.user.entity.Shop;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
public class Subscription extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan plan;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal amountPaid;

    private String billingCycle; // MONTHLY, YEARLY

    private String status; // ACTIVE, EXPIRED, CANCELLED
}
