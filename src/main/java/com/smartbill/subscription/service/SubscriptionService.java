package com.smartbill.subscription.service;

import com.smartbill.exception.AppException;
import com.smartbill.subscription.dto.SubscribeRequest;
import com.smartbill.subscription.entity.Subscription;
import com.smartbill.subscription.entity.SubscriptionPlan;
import com.smartbill.subscription.repository.SubscriptionPlanRepository;
import com.smartbill.subscription.repository.SubscriptionRepository;
import com.smartbill.user.entity.Shop;
import com.smartbill.user.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository planRepository;
    private final ShopRepository shopRepository;

    public List<SubscriptionPlan> getPlans() {
        return planRepository.findByActiveTrue();
    }

    @Transactional
    public Subscription subscribe(Long shopId, SubscribeRequest req) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException("Shop not found", HttpStatus.NOT_FOUND));

        SubscriptionPlan plan = planRepository.findById(req.getPlanId())
                .orElseThrow(() -> new AppException("Plan not found", HttpStatus.NOT_FOUND));

        boolean yearly = "YEARLY".equalsIgnoreCase(req.getBillingCycle());
        LocalDate start = LocalDate.now();
        LocalDate end = yearly ? start.plusYears(1) : start.plusMonths(1);
        BigDecimal amount = yearly ? plan.getYearlyPrice() : plan.getMonthlyPrice();

        Subscription subscription = new Subscription();
        subscription.setShop(shop);
        subscription.setPlan(plan);
        subscription.setStartDate(start);
        subscription.setEndDate(end);
        subscription.setAmountPaid(amount);
        subscription.setBillingCycle(req.getBillingCycle().toUpperCase());
        subscription.setStatus("ACTIVE");
        subscriptionRepository.save(subscription);

        shop.setSubscriptionEndDate(end);
        shopRepository.save(shop);

        return subscription;
    }

    public List<Subscription> getHistory(Long shopId) {
        return subscriptionRepository.findByShopIdOrderByCreatedAtDesc(shopId);
    }
}
