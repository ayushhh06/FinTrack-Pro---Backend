package com.tracker.expense_api.repository;

import com.tracker.expense_api.model.Expense;
import com.tracker.expense_api.dto.CategorySummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e.category as category, SUM(e.amount) as totalAmount " +
            "FROM Expense e WHERE e.user.id = :userId GROUP BY e.category")
    List<CategorySummary> getExpensesByCategorySummary(@Param("userId") Long userId);


    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId AND " +
            "(LOWER(e.category) = LOWER(COALESCE(:category, e.category))) AND " +
            "(:startDate IS NULL OR e.date >= :startDate) AND " +
            "(:endDate IS NULL OR e.date <= :endDate)")
    Page<Expense> filterExpenses(
            @Param("userId") Long userId,
            @Param("category") String category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
}