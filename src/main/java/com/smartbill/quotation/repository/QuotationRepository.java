package com.smartbill.quotation.repository;

import com.smartbill.quotation.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    List<Quotation> findByShopIdOrderByCreatedAtDesc(Long shopId);
    Optional<Quotation> findByIdAndShopId(Long id, Long shopId);
    long countByShopId(Long shopId);
}
