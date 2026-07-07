package com.smartbill.user.entity;

import com.smartbill.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String mobile;

    private String password;
}
