package com.smartbill.user.entity;

import com.smartbill.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "shops")
@Getter
@Setter
public class Shop extends BaseEntity {

    @Column(nullable = false)
    private String shopName;

    private String businessType;

    private String address;

    private String gstNumber;

    private String country;

    private String currency;

    private LocalDate trialEndDate;

    private LocalDate subscriptionEndDate;

    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public boolean isInTrial() {
        return trialEndDate != null && !LocalDate.now().isAfter(trialEndDate);
    }

    public boolean hasActiveSubscription() {
        return subscriptionEndDate != null && !LocalDate.now().isAfter(subscriptionEndDate);
    }

    public boolean canAccess() {
        return active && (isInTrial() || hasActiveSubscription());
    }
}
