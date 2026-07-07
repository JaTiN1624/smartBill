package com.smartbill.purchase.service;

import com.smartbill.exception.AppException;
import com.smartbill.product.entity.Product;
import com.smartbill.product.repository.ProductRepository;
import com.smartbill.purchase.dto.PurchaseRequest;
import com.smartbill.purchase.dto.PurchaseResponse;
import com.smartbill.purchase.dto.VendorRequest;
import com.smartbill.purchase.dto.VendorResponse;
import com.smartbill.purchase.entity.PurchaseOrder;
import com.smartbill.purchase.entity.Vendor;
import com.smartbill.purchase.repository.PurchaseOrderRepository;
import com.smartbill.purchase.repository.VendorRepository;
import com.smartbill.user.entity.Shop;
import com.smartbill.user.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    public VendorResponse createVendor(Long shopId, VendorRequest req) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException("Shop not found"));
        Vendor v = new Vendor();
        v.setName(req.getName());
        v.setMobile(req.getMobile());
        v.setEmail(req.getEmail());
        v.setAddress(req.getAddress());
        v.setGstNumber(req.getGstNumber());
        v.setShop(shop);
        return toVendorResponse(vendorRepository.save(v));
    }

    public List<VendorResponse> getVendors(Long shopId) {
        return vendorRepository.findByShopId(shopId)
                .stream().map(this::toVendorResponse).collect(Collectors.toList());
    }

    @Transactional
    public PurchaseResponse createPurchase(Long shopId, PurchaseRequest req) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException("Shop not found"));
        Product product = productRepository.findByIdAndShopId(req.getProductId(), shopId)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        PurchaseOrder po = new PurchaseOrder();
        po.setProduct(product);
        po.setQuantity(req.getQuantity());
        po.setPurchasePrice(req.getPurchasePrice());
        po.setTotalAmount(req.getPurchasePrice().multiply(BigDecimal.valueOf(req.getQuantity())));
        po.setNotes(req.getNotes());
        po.setPurchaseDate(req.getPurchaseDate() != null ? req.getPurchaseDate() : LocalDate.now());
        po.setShop(shop);

        if (req.getVendorId() != null) {
            Vendor vendor = vendorRepository.findByIdAndShopId(req.getVendorId(), shopId)
                    .orElseThrow(() -> new AppException("Vendor not found"));
            po.setVendor(vendor);
        }

        // Update product stock
        product.setStockQuantity(product.getStockQuantity() + req.getQuantity());
        productRepository.save(product);

        return toPurchaseResponse(purchaseOrderRepository.save(po));
    }

    public List<PurchaseResponse> getAllPurchases(Long shopId) {
        return purchaseOrderRepository.findByShopIdOrderByPurchaseDateDesc(shopId)
                .stream().map(this::toPurchaseResponse).collect(Collectors.toList());
    }

    private VendorResponse toVendorResponse(Vendor v) {
        return new VendorResponse(v.getId(), v.getName(), v.getMobile(), v.getEmail(), v.getAddress(), v.getGstNumber());
    }

    private PurchaseResponse toPurchaseResponse(PurchaseOrder po) {
        String vendorName = po.getVendor() != null ? po.getVendor().getName() : null;
        String productName = po.getProduct() != null ? po.getProduct().getName() : null;
        return new PurchaseResponse(po.getId(), vendorName, productName, po.getQuantity(),
                po.getPurchasePrice(), po.getTotalAmount(), po.getNotes(), po.getPurchaseDate(), po.getCreatedAt());
    }
}
