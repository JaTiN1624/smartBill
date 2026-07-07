package com.smartbill.user.service;

import com.smartbill.exception.AppException;
import com.smartbill.user.dto.ShopRequest;
import com.smartbill.user.dto.ShopResponse;
import com.smartbill.user.entity.Shop;
import com.smartbill.user.entity.User;
import com.smartbill.user.repository.ShopRepository;
import com.smartbill.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    public ShopResponse getShop(Long shopId, Long userId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException("Shop not found", HttpStatus.NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        return toResponse(shop, user);
    }

    public ShopResponse updateShop(Long shopId, Long userId, ShopRequest req) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException("Shop not found", HttpStatus.NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        shop.setShopName(req.getShopName());
        if (req.getBusinessType() != null) shop.setBusinessType(req.getBusinessType());
        if (req.getAddress() != null) shop.setAddress(req.getAddress());
        if (req.getGstNumber() != null) shop.setGstNumber(req.getGstNumber());
        if (req.getCurrency() != null) shop.setCurrency(req.getCurrency());
        if (req.getMobile() != null) user.setMobile(req.getMobile());

        userRepository.save(user);
        return toResponse(shopRepository.save(shop), user);
    }

    private ShopResponse toResponse(Shop shop, User user) {
        return new ShopResponse(
                shop.getId(),
                shop.getShopName(),
                shop.getBusinessType(),
                shop.getAddress(),
                shop.getGstNumber(),
                shop.getCurrency(),
                user.getFullName(),
                user.getEmail(),
                user.getMobile(),
                shop.getTrialEndDate(),
                shop.getSubscriptionEndDate(),
                shop.isInTrial(),
                shop.hasActiveSubscription()
        );
    }
}
