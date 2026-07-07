package com.smartbill.product.service;

import com.smartbill.exception.AppException;
import com.smartbill.product.dto.CategoryRequest;
import com.smartbill.product.dto.CategoryResponse;
import com.smartbill.product.entity.Category;
import com.smartbill.product.repository.CategoryRepository;
import com.smartbill.user.entity.Shop;
import com.smartbill.user.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;

    public CategoryResponse createCategory(Long shopId, CategoryRequest req) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException("Shop not found"));
        Category cat = new Category();
        cat.setName(req.getName());
        cat.setShop(shop);
        return toResponse(categoryRepository.save(cat));
    }

    public List<CategoryResponse> getAllCategories(Long shopId) {
        return categoryRepository.findByShopId(shopId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public void deleteCategory(Long shopId, Long id) {
        Category cat = categoryRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new AppException("Category not found", HttpStatus.NOT_FOUND));
        categoryRepository.delete(cat);
    }

    private CategoryResponse toResponse(Category c) {
        return new CategoryResponse(c.getId(), c.getName());
    }
}
