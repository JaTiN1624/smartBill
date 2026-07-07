package com.smartbill.expense.repository;

import com.smartbill.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByShopIdOrderByExpenseDateDesc(Long shopId);
    Optional<Expense> findByIdAndShopId(Long id, Long shopId);
}
