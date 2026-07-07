package com.smartbill.config;

import com.smartbill.subscription.entity.SubscriptionPlan;
import com.smartbill.subscription.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SubscriptionPlanRepository planRepository;

    @Override
    public void run(String... args) {
        if (planRepository.count() == 0) {
            planRepository.saveAll(List.of(
                    buildPlan("Basic", new BigDecimal("499"), new BigDecimal("4999"), 100, 500),
                    buildPlan("Professional", new BigDecimal("999"), new BigDecimal("9999"), 500, 2000),
                    buildPlan("Enterprise", new BigDecimal("1999"), new BigDecimal("19999"), -1, -1)
            ));
        }
    }

    private SubscriptionPlan buildPlan(String name, BigDecimal monthly, BigDecimal yearly, int maxProducts, int maxOrders) {
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setPlanName(name);
        plan.setMonthlyPrice(monthly);
        plan.setYearlyPrice(yearly);
        plan.setMaxProducts(maxProducts);
        plan.setMaxOrders(maxOrders);
        plan.setActive(true);
        return plan;
    }
}
