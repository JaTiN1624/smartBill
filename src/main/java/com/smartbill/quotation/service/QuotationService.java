package com.smartbill.quotation.service;

import com.smartbill.customer.entity.Customer;
import com.smartbill.customer.repository.CustomerRepository;
import com.smartbill.exception.AppException;
import com.smartbill.product.entity.Product;
import com.smartbill.product.repository.ProductRepository;
import com.smartbill.quotation.dto.QuotationItemRequest;
import com.smartbill.quotation.dto.QuotationRequest;
import com.smartbill.quotation.dto.QuotationResponse;
import com.smartbill.quotation.entity.Quotation;
import com.smartbill.quotation.entity.QuotationItem;
import com.smartbill.quotation.entity.QuotationStatus;
import com.smartbill.quotation.repository.QuotationRepository;
import com.smartbill.user.entity.Shop;
import com.smartbill.user.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    @Transactional
    public QuotationResponse create(Long shopId, QuotationRequest req) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException("Shop not found"));

        Quotation q = new Quotation();
        q.setShop(shop);
        q.setNotes(req.getNotes());
        q.setValidUntil(req.getValidUntil());
        q.setDiscountAmount(req.getDiscountAmount() != null ? req.getDiscountAmount() : BigDecimal.ZERO);

        if (req.getCustomerId() != null) {
            Customer customer = customerRepository.findByIdAndShopId(req.getCustomerId(), shopId)
                    .orElseThrow(() -> new AppException("Customer not found"));
            q.setCustomer(customer);
        }

        long count = quotationRepository.countByShopId(shopId) + 1;
        q.setQuotationNumber("QT-" + String.format("%04d", count));

        List<QuotationItem> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;

        for (QuotationItemRequest ir : req.getItems()) {
            QuotationItem item = new QuotationItem();
            item.setQuantity(ir.getQuantity());
            item.setUnitPrice(ir.getUnitPrice());
            BigDecimal tax = ir.getTaxPercent() != null ? ir.getTaxPercent() : BigDecimal.ZERO;
            item.setTaxPercent(tax);

            if (ir.getProductId() != null) {
                Product product = productRepository.findByIdAndShopId(ir.getProductId(), shopId)
                        .orElseThrow(() -> new AppException("Product not found"));
                item.setProduct(product);
                item.setProductName(product.getName());
            } else {
                item.setProductName(ir.getProductName());
            }

            BigDecimal lineTotal = ir.getUnitPrice().multiply(BigDecimal.valueOf(ir.getQuantity()));
            BigDecimal lineTax = lineTotal.multiply(tax).divide(BigDecimal.valueOf(100));
            item.setTotalPrice(lineTotal.add(lineTax));
            subtotal = subtotal.add(lineTotal);
            taxAmount = taxAmount.add(lineTax);
            item.setQuotation(q);
            items.add(item);
        }

        q.setItems(items);
        q.setSubtotal(subtotal);
        q.setTaxAmount(taxAmount);
        BigDecimal discount = q.getDiscountAmount();
        q.setTotalAmount(subtotal.add(taxAmount).subtract(discount));

        return toResponse(quotationRepository.save(q));
    }

    public List<QuotationResponse> getAll(Long shopId) {
        return quotationRepository.findByShopIdOrderByCreatedAtDesc(shopId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public QuotationResponse getById(Long shopId, Long id) {
        return toResponse(quotationRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new AppException("Quotation not found", HttpStatus.NOT_FOUND)));
    }

    public QuotationResponse updateStatus(Long shopId, Long id, QuotationStatus status) {
        Quotation q = quotationRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new AppException("Quotation not found", HttpStatus.NOT_FOUND));
        q.setStatus(status);
        return toResponse(quotationRepository.save(q));
    }

    private QuotationResponse toResponse(Quotation q) {
        String customerName = q.getCustomer() != null ? q.getCustomer().getName() : null;
        String customerMobile = q.getCustomer() != null ? q.getCustomer().getMobile() : null;
        List<QuotationResponse.QuotationItemDto> itemDtos = q.getItems() == null ? List.of() :
                q.getItems().stream().map(i -> new QuotationResponse.QuotationItemDto(
                        i.getId(), i.getProductName(), i.getQuantity(),
                        i.getUnitPrice(), i.getTaxPercent(), i.getTotalPrice()
                )).collect(Collectors.toList());
        return new QuotationResponse(q.getId(), q.getQuotationNumber(), customerName, customerMobile,
                itemDtos, q.getSubtotal(), q.getTaxAmount(), q.getDiscountAmount(),
                q.getTotalAmount(), q.getNotes(), q.getValidUntil(), q.getStatus(), q.getCreatedAt());
    }
}
