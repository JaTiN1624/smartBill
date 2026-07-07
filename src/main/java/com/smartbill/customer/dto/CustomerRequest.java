package com.smartbill.customer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {

    @NotBlank(message = "Customer name is required")
    private String name;

    private String mobile;

    private String email;

    private String address;

    private String gstNumber;
}
