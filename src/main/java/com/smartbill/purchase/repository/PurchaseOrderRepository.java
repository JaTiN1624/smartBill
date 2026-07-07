package com.smartbill.purchase.repository;

import com.smartbill.purchase.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByShopIdOrderByPurchaseDateDesc(Long shopId);
    Optional<PurchaseOrder> findByIdAndShopId(Long id, Long shopId);
}
