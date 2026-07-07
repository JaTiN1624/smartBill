package com.smartbill.order.repository;

import com.smartbill.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
            SELECT oi.product.id, oi.productName, SUM(oi.quantity), SUM(oi.totalPrice)
            FROM OrderItem oi
            WHERE oi.order.shop.id = :shopId
            AND oi.order.createdAt BETWEEN :from AND :to
            AND oi.order.orderStatus != 'CANCELLED'
            GROUP BY oi.product.id, oi.productName
            ORDER BY SUM(oi.quantity) DESC
            """)
    List<Object[]> findTopProducts(
            @Param("shopId") Long shopId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
