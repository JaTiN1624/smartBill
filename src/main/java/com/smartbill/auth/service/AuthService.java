package com.smartbill.auth.service;

import com.smartbill.auth.dto.AuthResponse;
import com.smartbill.auth.dto.LoginRequest;
import com.smartbill.auth.dto.RegisterRequest;
import com.smartbill.exception.AppException;
import com.smartbill.security.JwtUtil;
import com.smartbill.user.entity.Shop;
import com.smartbill.user.entity.User;
import com.smartbill.user.repository.ShopRepository;
import com.smartbill.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${app.trial.days:30}")
    private int trialDays;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new AppException("Email already registered", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setMobile(req.getMobile());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user = userRepository.save(user);

        Shop shop = new Shop();
        shop.setShopName(req.getShopName());
        shop.setBusinessType(req.getBusinessType());
        shop.setAddress(req.getAddress());
        shop.setGstNumber(req.getGstNumber());
        shop.setCountry(req.getCountry());
        shop.setCurrency(req.getCurrency());
        shop.setTrialEndDate(LocalDate.now().plusDays(trialDays));
        shop.setOwner(user);
        shop = shopRepository.save(shop);

        String token = jwtUtil.generateToken(user.getId(), shop.getId(), user.getEmail());
        return buildResponse(token, user, shop);
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new AppException("Invalid email or password", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new AppException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        Shop shop = shopRepository.findByOwnerId(user.getId()).stream()
                .findFirst()
                .orElseThrow(() -> new AppException("No shop found for this account", HttpStatus.NOT_FOUND));

        if (!shop.canAccess()) {
            throw new AppException("Your trial has expired. Please subscribe to continue.", HttpStatus.PAYMENT_REQUIRED);
        }

        String token = jwtUtil.generateToken(user.getId(), shop.getId(), user.getEmail());
        return buildResponse(token, user, shop);
    }

    private AuthResponse buildResponse(String token, User user, Shop shop) {
        return new AuthResponse(
                token,
                user.getId(),
                shop.getId(),
                user.getFullName(),
                shop.getShopName(),
                shop.getCurrency(),
                shop.getTrialEndDate(),
                shop.getSubscriptionEndDate(),
                shop.isInTrial(),
                shop.hasActiveSubscription()
        );
    }
}
