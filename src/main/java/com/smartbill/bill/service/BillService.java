package com.smartbill.bill.service;

import com.smartbill.bill.dto.BillResponse;
import com.smartbill.bill.entity.Bill;
import com.smartbill.bill.repository.BillRepository;
import com.smartbill.exception.AppException;
import com.smartbill.order.dto.OrderItemResponse;
import com.smartbill.order.entity.Order;
import com.smartbill.order.repository.OrderRepository;
import com.smartbill.user.entity.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public BillResponse generateBill(Long shopId, Long orderId) {
        Order order = orderRepository.findByIdAndShopId(orderId, shopId)
                .orElseThrow(() -> new AppException("Order not found", HttpStatus.NOT_FOUND));

        return billRepository.findByOrderId(orderId)
                .map(this::toResponse)
                .orElseGet(() -> {
                    Bill bill = new Bill();
                    bill.setOrder(order);
                    bill.setShop(order.getShop());
                    bill.setBillNumber(generateBillNumber(shopId));
                    bill.setSubtotal(order.getSubtotal());
                    bill.setTaxAmount(order.getTaxAmount());
                    bill.setDiscountAmount(order.getDiscountAmount());
                    bill.setTotalAmount(order.getTotalAmount());
                    bill.setPaymentMethod(order.getPaymentMethod());
                    bill.setPaymentStatus(order.getPaymentStatus());
                    bill.setNotes(order.getNotes());

                    if (order.getCustomer() != null) {
                        bill.setCustomerName(order.getCustomer().getName());
                        bill.setCustomerMobile(order.getCustomer().getMobile());
                        bill.setCustomerAddress(order.getCustomer().getAddress());
                    } else {
                        bill.setCustomerName("Walk-in Customer");
                    }

                    return toResponse(billRepository.save(bill));
                });
    }

    public BillResponse getBill(Long shopId, Long billId) {
        Bill bill = billRepository.findByIdAndShopId(billId, shopId)
                .orElseThrow(() -> new AppException("Bill not found", HttpStatus.NOT_FOUND));
        return toResponse(bill);
    }

    public List<BillResponse> getAllBills(Long shopId) {
        return billRepository.findByShopIdOrderByCreatedAtDesc(shopId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private String generateBillNumber(Long shopId) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = billRepository.findByShopIdOrderByCreatedAtDesc(shopId).size() + 1;
        return String.format("BILL-%d-%s-%04d", shopId, date, count);
    }

    private BillResponse toResponse(Bill bill) {
        Shop shop = bill.getShop();
        Order order = bill.getOrder();

        List<OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderItemResponse(i.getId(), i.getProduct().getId(),
                        i.getProductName(), i.getQuantity(), i.getUnitPrice(),
                        i.getTaxPercent(), i.getTotalPrice()))
                .collect(Collectors.toList());

        return new BillResponse(
                bill.getId(), bill.getBillNumber(), order.getOrderNumber(), order.getId(),
                shop.getShopName(), shop.getAddress(), shop.getGstNumber(), shop.getCurrency(),
                bill.getCustomerName(), bill.getCustomerMobile(), bill.getCustomerAddress(),
                items, bill.getSubtotal(), bill.getTaxAmount(), bill.getDiscountAmount(),
                bill.getTotalAmount(), bill.getPaymentMethod(), bill.getPaymentStatus(),
                bill.getNotes(), bill.getCreatedAt()
        );
    }
}
