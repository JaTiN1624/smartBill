package com.smartbill.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private String name;
    private String mobile;
    private String email;
    private String address;
    private String gstNumber;
}
