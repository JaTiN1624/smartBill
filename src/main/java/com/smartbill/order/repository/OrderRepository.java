package com.smartbill.order.repository;

import com.smartbill.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByShopIdOrderByCreatedAtDesc(Long shopId);

    Optional<Order> findByIdAndShopId(Long id, Long shopId);

    List<Order> findByShopIdAndCustomerIdOrderByCreatedAtDesc(Long shopId, Long customerId);

    @Query("SELECT o FROM Order o WHERE o.shop.id = :shopId AND o.createdAt BETWEEN :from AND :to ORDER BY o.createdAt DESC")
    List<Order> findByShopIdAndDateRange(
            @Param("shopId") Long shopId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.shop.id = :shopId AND o.createdAt BETWEEN :from AND :to AND o.orderStatus != 'CANCELLED'")
    Long countByShopAndDateRange(@Param("shopId") Long shopId, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.shop.id = :shopId AND o.createdAt BETWEEN :from AND :to AND o.orderStatus != 'CANCELLED'")
    java.math.BigDecimal sumRevenueByShopAndDateRange(@Param("shopId") Long shopId, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
