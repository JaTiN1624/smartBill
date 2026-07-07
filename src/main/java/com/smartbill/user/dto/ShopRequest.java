package com.smartbill.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopRequest {
    @NotBlank
    private String shopName;
    private String businessType;
    private String address;
    private String gstNumber;
    private String mobile;
    private String currency;
}
