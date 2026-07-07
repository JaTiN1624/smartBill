package com.smartbill.product.repository;

import com.smartbill.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByShopIdAndActiveTrue(Long shopId);
    Optional<Product> findByIdAndShopId(Long id, Long shopId);
    List<Product> findByShopIdAndNameContainingIgnoreCaseAndActiveTrue(Long shopId, String name);

    @Query("SELECT p FROM Product p WHERE p.shop.id = :shopId AND p.stockQuantity <= :threshold AND p.active = true")
    List<Product> findLowStock(@Param("shopId") Long shopId, @Param("threshold") int threshold);
}
