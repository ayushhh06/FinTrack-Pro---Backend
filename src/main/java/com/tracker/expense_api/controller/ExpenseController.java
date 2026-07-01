package com.tracker.expense_api.controller;

import com.tracker.expense_api.model.Expense;
import com.tracker.expense_api.dto.CategorySummary;
import com.tracker.expense_api.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "http://localhost:5173")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<Page<Expense>> getExpenses(
            @RequestAttribute("authenticatedUserId") Long userId, // Securely pulled straight out of verified JWT claim attribute
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Page<Expense> expenses = expenseService.getFilteredExpenses(userId, category, startDate, endDate, page, size, sortBy, direction);
        return ResponseEntity.ok(expenses);
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(
            @RequestAttribute("authenticatedUserId") Long userId,
            @Valid @RequestBody Expense expense) {
        Expense savedExpense = expenseService.saveExpense(userId, expense);
        return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
    }

    @GetMapping("/summary")
    public List<CategorySummary> getSummary(@RequestAttribute("authenticatedUserId") Long userId) {
        return expenseService.getCategorySummaries(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(
            @PathVariable Long id,
            @RequestAttribute("authenticatedUserId") Long userId,
            @Valid @RequestBody Expense expenseDetails) {
        Expense updatedExpense = expenseService.updateExpense(id, userId, expenseDetails);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(
            @PathVariable Long id,
            @RequestAttribute("authenticatedUserId") Long userId) {
        expenseService.deleteExpense(id, userId);
        return ResponseEntity.ok("Deleted successfully");
    }
}