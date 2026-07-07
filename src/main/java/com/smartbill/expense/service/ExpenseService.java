package com.smartbill.expense.service;

import com.smartbill.exception.AppException;
import com.smartbill.expense.dto.ExpenseRequest;
import com.smartbill.expense.dto.ExpenseResponse;
import com.smartbill.expense.entity.Expense;
import com.smartbill.expense.repository.ExpenseRepository;
import com.smartbill.user.entity.Shop;
import com.smartbill.user.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ShopRepository shopRepository;

    public ExpenseResponse create(Long shopId, ExpenseRequest req) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new AppException("Shop not found"));
        Expense e = new Expense();
        e.setTitle(req.getTitle());
        e.setCategory(req.getCategory());
        e.setAmount(req.getAmount());
        e.setNotes(req.getNotes());
        e.setExpenseDate(req.getExpenseDate() != null ? req.getExpenseDate() : LocalDate.now());
        e.setShop(shop);
        return toResponse(expenseRepository.save(e));
    }

    public List<ExpenseResponse> getAll(Long shopId) {
        return expenseRepository.findByShopIdOrderByExpenseDateDesc(shopId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public void delete(Long shopId, Long id) {
        Expense e = expenseRepository.findByIdAndShopId(id, shopId)
                .orElseThrow(() -> new AppException("Expense not found", HttpStatus.NOT_FOUND));
        expenseRepository.delete(e);
    }

    private ExpenseResponse toResponse(Expense e) {
        return new ExpenseResponse(e.getId(), e.getTitle(), e.getCategory(),
                e.getAmount(), e.getNotes(), e.getExpenseDate(), e.getCreatedAt());
    }
}
