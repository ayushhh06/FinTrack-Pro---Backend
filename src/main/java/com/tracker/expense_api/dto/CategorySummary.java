package com.tracker.expense_api.dto;

import java.math.BigDecimal;

public interface CategorySummary {
    String getCategory();
    BigDecimal getTotalAmount();
}