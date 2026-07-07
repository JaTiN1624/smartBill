package com.smartbill.purchase.repository;

import com.smartbill.purchase.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    List<Vendor> findByShopId(Long shopId);
    Optional<Vendor> findByIdAndShopId(Long id, Long shopId);
}
