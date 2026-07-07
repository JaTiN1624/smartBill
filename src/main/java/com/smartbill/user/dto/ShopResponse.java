package com.smartbill.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ShopResponse {
    private Long id;
    private String shopName;
    private String businessType;
    private String address;
    private String gstNumber;
    private String currency;
    private String ownerName;
    private String ownerEmail;
    private String ownerMobile;
    private LocalDate trialEndDate;
    private LocalDate subscriptionEndDate;
    private boolean inTrial;
    private boolean hasActiveSubscription;
}
