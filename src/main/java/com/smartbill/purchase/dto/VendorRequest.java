package com.smartbill.purchase.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorRequest {
    @NotBlank
    private String name;
    private String mobile;
    private String email;
    private String address;
    private String gstNumber;
}
