package com.smartbill.customer.repository;

import com.smartbill.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByShopId(Long shopId);
    Optional<Customer> findByIdAndShopId(Long id, Long shopId);
    List<Customer> findByShopIdAndNameContainingIgnoreCase(Long shopId, String name);
}
