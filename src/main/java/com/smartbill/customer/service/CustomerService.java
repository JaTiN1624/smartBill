package com.smartbill.customer.service;

import com.smartbill.customer.dto.CustomerLedgerResponse;
import com.smartbill.customer.dto.CustomerRequest;
import com.smartbill.customer.dto.CustomerResponse;
import com.smartbill.customer.entity.Customer;
import com.smartbill.customer.repository.CustomerRepository;
import com.smartbill.exception.AppException;
import com.smartbill.order.repository.OrderRepository;
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
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;
    private final OrderRepository orderRepository;

    public CustomerResponse create(Long shopId, CustomerRequest req) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException("Shop not found"));
        Customer customer = new Customer();
        customer.setName(req.getName());
        customer.setMobile(req.getMobile());
        customer.setEmail(req.getEmail());
        customer.setAddress(req.getAddress());
        customer.setGstNumber(req.getGstNumber());
        customer.setShop(shop);
        return toResponse(customerRepository.save(customer));
    }

    public List<CustomerResponse> getAll(Long shopId, String search) {
        List<Customer> customers = (search != null && !search.isBlank())
                ? customerRepository.findByShopIdAndNameContainingIgnoreCase(shopId, search)
                : customerRepository.findByShopId(shopId);
        return customers.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CustomerResponse getById(Long shopId, Long id) {
        Customer c = customerRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        return toResponse(c);
    }

    public CustomerResponse update(Long shopId, Long id, CustomerRequest req) {
        Customer c = customerRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        c.setName(req.getName());
        c.setMobile(req.getMobile());
        c.setEmail(req.getEmail());
        c.setAddress(req.getAddress());
        c.setGstNumber(req.getGstNumber());
        return toResponse(customerRepository.save(c));
    }

    public void delete(Long shopId, Long id) {
        Customer c = customerRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        customerRepository.delete(c);
    }

    public CustomerLedgerResponse getLedger(Long shopId, Long customerId) {
        Customer c = customerRepository.findByIdAndShopId(customerId, shopId)
                .orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        var orders = orderRepository.findByShopIdAndCustomerIdOrderByCreatedAtDesc(shopId, customerId);
        BigDecimal totalBilled = orders.stream()
                .filter(o -> !o.getOrderStatus().equals("CANCELLED"))
                .map(o -> o.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPaid = orders.stream()
                .filter(o -> "PAID".equals(o.getPaymentStatus()))
                .map(o -> o.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<CustomerLedgerResponse.LedgerEntry> entries = orders.stream()
                .map(o -> new CustomerLedgerResponse.LedgerEntry(
                        o.getId(), o.getOrderNumber(), o.getTotalAmount(),
                        o.getPaymentStatus(), o.getOrderStatus(), o.getCreatedAt()))
                .collect(Collectors.toList());
        return new CustomerLedgerResponse(c.getId(), c.getName(), c.getMobile(),
                totalBilled, totalPaid, totalBilled.subtract(totalPaid), orders.size(), entries);
    }

    private CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(c.getId(), c.getName(), c.getMobile(), c.getEmail(), c.getAddress(), c.getGstNumber());
    }
}
