package com.smartbill.customer.entity;

import com.smartbill.common.BaseEntity;
import com.smartbill.user.entity.Shop;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String mobile;

    private String email;

    private String address;

    private String gstNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;
}
