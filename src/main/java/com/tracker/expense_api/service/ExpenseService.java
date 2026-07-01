package com.tracker.expense_api.service;

import com.tracker.expense_api.model.Expense;
import com.tracker.expense_api.model.User;
import com.tracker.expense_api.dto.CategorySummary;
import com.tracker.expense_api.repository.ExpenseRepository;
import com.tracker.expense_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<Expense> getFilteredExpenses(Long userId, String category, LocalDate startDate, LocalDate endDate,
                                             int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.DESC.name()) ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return expenseRepository.filterExpenses(userId, category, startDate, endDate, pageable);
    }

    public Expense saveExpense(Long userId, Expense expense) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User profile not found with id: " + userId));
        expense.setUser(user);
        return expenseRepository.save(expense);
    }

    public List<CategorySummary> getCategorySummaries(Long userId) {
        return expenseRepository.getExpensesByCategorySummary(userId);
    }

    public Expense updateExpense(Long id, Long userId, Expense expenseDetails) {
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense record not found with id: " + id));


        if (!existingExpense.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized data mutation attempt.");
        }

        existingExpense.setDescription(expenseDetails.getDescription());
        existingExpense.setAmount(expenseDetails.getAmount());
        existingExpense.setCategory(expenseDetails.getCategory());
        existingExpense.setDate(expenseDetails.getDate());

        return expenseRepository.save(existingExpense);
    }

    public void deleteExpense(Long id, Long userId) {
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense record not found with id: " + id));


        if (!existingExpense.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized data mutation attempt.");
        }

        expenseRepository.delete(existingExpense);
    }
}