package com.smartbill.product.repository;

import com.smartbill.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByShopId(Long shopId);
    Optional<Category> findByIdAndShopId(Long id, Long shopId);
}
