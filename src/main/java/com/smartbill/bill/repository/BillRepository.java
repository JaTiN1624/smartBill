package com.smartbill.bill.repository;

import com.smartbill.bill.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByOrderId(Long orderId);
    Optional<Bill> findByIdAndShopId(Long id, Long shopId);
    List<Bill> findByShopIdOrderByCreatedAtDesc(Long shopId);
    Optional<Bill> findByBillNumber(String billNumber);
}
