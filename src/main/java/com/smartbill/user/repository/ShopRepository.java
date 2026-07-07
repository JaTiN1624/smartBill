package com.smartbill.user.repository;

import com.smartbill.user.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByOwnerId(Long ownerId);
    Optional<Shop> findByIdAndOwnerId(Long id, Long ownerId);
}
