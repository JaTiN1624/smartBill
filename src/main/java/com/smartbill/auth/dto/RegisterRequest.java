package com.smartbill.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String mobile;

    @NotBlank(message = "Shop name is required")
    private String shopName;

    private String businessType;

    private String address;

    private String gstNumber;

    private String country = "India";

    private String currency = "INR";
}
