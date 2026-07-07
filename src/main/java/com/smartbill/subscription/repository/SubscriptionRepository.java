package com.smartbill.subscription.repository;

import com.smartbill.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByShopIdOrderByCreatedAtDesc(Long shopId);
    Optional<Subscription> findTopByShopIdAndStatusOrderByEndDateDesc(Long shopId, String status);
}
