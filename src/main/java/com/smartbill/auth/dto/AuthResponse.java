package com.smartbill.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private Long shopId;
    private String fullName;
    private String shopName;
    private String currency;
    private LocalDate trialEndDate;
    private LocalDate subscriptionEndDate;
    private boolean inTrial;
    private boolean subscriptionActive;
}
