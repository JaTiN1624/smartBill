package com.smartbill.product.service;

import com.smartbill.exception.AppException;
import com.smartbill.product.dto.ProductRequest;
import com.smartbill.product.dto.ProductResponse;
import com.smartbill.product.entity.Category;
import com.smartbill.product.entity.Product;
import com.smartbill.product.repository.CategoryRepository;
import com.smartbill.product.repository.ProductRepository;
import com.smartbill.user.entity.Shop;
import com.smartbill.user.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;

    public ProductResponse createProduct(Long shopId, ProductRequest req) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException("Shop not found"));
        Product product = new Product();
        fillProduct(product, req, shopId);
        product.setShop(shop);
        return toResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAllProducts(Long shopId, String search) {
        List<Product> products = (search != null && !search.isBlank())
                ? productRepository.findByShopIdAndNameContainingIgnoreCaseAndActiveTrue(shopId, search)
                : productRepository.findByShopIdAndActiveTrue(shopId);
        return products.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long shopId, Long id) {
        return toResponse(productRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND)));
    }

    public ProductResponse updateProduct(Long shopId, Long id, ProductRequest req) {
        Product product = productRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));
        fillProduct(product, req, shopId);
        return toResponse(productRepository.save(product));
    }

    public void deleteProduct(Long shopId, Long id) {
        Product product = productRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));
        product.setActive(false);
        productRepository.save(product);
    }

    public List<ProductResponse> getLowStock(Long shopId, int threshold) {
        return productRepository.findLowStock(shopId, threshold)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private void fillProduct(Product p, ProductRequest req, Long shopId) {
        p.setName(req.getName());
        p.setSku(req.getSku());
        p.setDescription(req.getDescription());
        p.setUnit(req.getUnit());
        p.setPrice(req.getPrice());
        p.setPurchasePrice(req.getPurchasePrice());
        p.setTaxPercent(req.getTaxPercent() != null ? req.getTaxPercent() : BigDecimal.ZERO);
        p.setStockQuantity(req.getStockQuantity() != null ? req.getStockQuantity() : 0);
        p.setHsnCode(req.getHsnCode());
        if (req.getCategoryId() != null) {
            Category cat = categoryRepository.findByIdAndShopId(req.getCategoryId(), shopId)
                    .orElseThrow(() -> new AppException("Category not found"));
            p.setCategory(cat);
        }
    }

    public ProductResponse toResponse(Product p) {
        String categoryName = p.getCategory() != null ? p.getCategory().getName() : null;
        return new ProductResponse(p.getId(), p.getName(), p.getSku(), p.getDescription(),
                p.getUnit(), p.getPrice(), p.getPurchasePrice(), p.getTaxPercent(),
                p.getStockQuantity(), categoryName, p.getHsnCode());
    }
}
