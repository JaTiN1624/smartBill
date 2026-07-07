package com.smartbill.order.service;

import com.smartbill.customer.entity.Customer;
import com.smartbill.customer.repository.CustomerRepository;
import com.smartbill.exception.AppException;
import com.smartbill.order.dto.*;
import com.smartbill.order.entity.Order;
import com.smartbill.order.entity.OrderItem;
import com.smartbill.order.repository.OrderRepository;
import com.smartbill.product.entity.Product;
import com.smartbill.product.repository.ProductRepository;
import com.smartbill.user.entity.Shop;
import com.smartbill.user.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;

    @Transactional
    public OrderResponse createOrder(Long shopId, OrderRequest req) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException("Shop not found"));

        Order order = new Order();
        order.setShop(shop);
        order.setOrderNumber(generateOrderNumber(shopId));
        order.setOrderStatus("CONFIRMED");
        order.setPaymentStatus(req.getPaymentStatus() != null ? req.getPaymentStatus() : "UNPAID");
        order.setPaymentMethod(req.getPaymentMethod());
        order.setNotes(req.getNotes());

        if (req.getCustomerId() != null) {
            Customer customer = customerRepository.findByIdAndShopId(req.getCustomerId(), shopId)
                    .orElseThrow(() -> new AppException("Customer not found"));
            order.setCustomer(customer);
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal taxTotal = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : req.getItems()) {
            Product product = productRepository.findByIdAndShopId(itemReq.getProductId(), shopId)
                    .orElseThrow(() -> new AppException("Product not found: " + itemReq.getProductId()));

            if (product.getStockQuantity() < itemReq.getQuantity()) {
                throw new AppException("Insufficient stock for: " + product.getName());
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            BigDecimal tax = itemTotal.multiply(product.getTaxPercent())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setProductName(product.getName());
            item.setUnitPrice(product.getPrice());
            item.setTaxPercent(product.getTaxPercent());
            item.setQuantity(itemReq.getQuantity());
            item.setTotalPrice(itemTotal.add(tax));
            order.getItems().add(item);

            product.setStockQuantity(product.getStockQuantity() - itemReq.getQuantity());
            productRepository.save(product);

            subtotal = subtotal.add(itemTotal);
            taxTotal = taxTotal.add(tax);
        }

        BigDecimal discount = req.getDiscountAmount() != null ? req.getDiscountAmount() : BigDecimal.ZERO;
        order.setSubtotal(subtotal);
        order.setTaxAmount(taxTotal);
        order.setDiscountAmount(discount);
        order.setTotalAmount(subtotal.add(taxTotal).subtract(discount));

        return toResponse(orderRepository.save(order));
    }

    public List<OrderResponse> getOrders(Long shopId) {
        return orderRepository.findByShopIdOrderByCreatedAtDesc(shopId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersFiltered(Long shopId, String from, String to, String status) {
        List<Order> orders;
        if (from != null && to != null) {
            LocalDateTime fromDt = LocalDate.parse(from).atStartOfDay();
            LocalDateTime toDt = LocalDate.parse(to).atTime(23, 59, 59);
            orders = orderRepository.findByShopIdAndDateRange(shopId, fromDt, toDt);
        } else {
            orders = orderRepository.findByShopIdOrderByCreatedAtDesc(shopId);
        }
        if (status != null && !status.isBlank()) {
            orders = orders.stream()
                    .filter(o -> o.getOrderStatus().equalsIgnoreCase(status) || o.getPaymentStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }
        return orders.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByCustomer(Long shopId, Long customerId) {
        return orderRepository.findByShopIdAndCustomerIdOrderByCreatedAtDesc(shopId, customerId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long shopId, Long id) {
        return toResponse(orderRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new AppException("Order not found", HttpStatus.NOT_FOUND)));
    }

    @Transactional
    public OrderResponse updateStatus(Long shopId, Long id, String status, String paymentStatus) {
        Order order = orderRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new AppException("Order not found", HttpStatus.NOT_FOUND));
        if (status != null) order.setOrderStatus(status);
        if (paymentStatus != null) order.setPaymentStatus(paymentStatus);
        return toResponse(orderRepository.save(order));
    }

    private String generateOrderNumber(Long shopId) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = orderRepository.countByShopAndDateRange(shopId,
                LocalDate.now().atStartOfDay(), LocalDate.now().atTime(23, 59, 59)) + 1;
        return String.format("ORD-%d-%s-%04d", shopId, date, count);
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderItemResponse(i.getId(), i.getProduct().getId(),
                        i.getProductName(), i.getQuantity(), i.getUnitPrice(),
                        i.getTaxPercent(), i.getTotalPrice()))
                .collect(Collectors.toList());

        String customerName = order.getCustomer() != null ? order.getCustomer().getName() : "Walk-in Customer";
        Long customerId = order.getCustomer() != null ? order.getCustomer().getId() : null;

        return new OrderResponse(order.getId(), order.getOrderNumber(), customerName, customerId,
                items, order.getSubtotal(), order.getTaxAmount(), order.getDiscountAmount(),
                order.getTotalAmount(), order.getOrderStatus(), order.getPaymentStatus(),
                order.getPaymentMethod(), order.getNotes(), order.getCreatedAt());
    }
}
