package com.expanse_tracker.service;

import com.expanse_tracker.controller.dto.TopCategoryDTO;
import com.expanse_tracker.enums.DateRangeType;
import com.expanse_tracker.models.CategoryEntity;
import com.expanse_tracker.models.UserEntity;
import com.expanse_tracker.records.DateRange;
import com.expanse_tracker.repository.ExpenseRepository;
import com.expanse_tracker.utils.DateRangeFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseAnalyticsService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    public ExpenseAnalyticsService(ExpenseRepository expenseRepository, UserService userService, CategoryService categoryService) {
        this.categoryService = categoryService;
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }

    public Double summary(String username, DateRangeType dateRangeType, String categoryName, LocalDate from, LocalDate to) {
        DateRange fechas = DateRangeFactory.dateRange(dateRangeType,from,to);

        if (categoryName != null && !categoryName.isEmpty()) {

            CategoryEntity categoryEntity = categoryService.findByCategory(categoryName);
            return expenseRepository.sumByUserUsernameAndCategoryAndDateBetween(username, categoryEntity, fechas.from(), fechas.to());
        }

        return expenseRepository.sumByUserUsernameAndDateBetween(username, fechas.from(), fechas.to());
    }

    public Double getVaration(String username){
        DateRange thisMonth = DateRangeFactory.from(DateRangeType.THIS_MONTH);
        DateRange lastMonth = DateRangeFactory.from(DateRangeType.LAST_MONTH);

        Double totalThisMonth = expenseRepository.getTotalBetweenDates(username, thisMonth.from(), thisMonth.to());
        Double totalLastMonth = expenseRepository.getTotalBetweenDates(username, lastMonth.from(), lastMonth.to());

        double variation = 0.0;

        if (totalLastMonth != 0) {
            variation = ((totalThisMonth - totalLastMonth) / totalLastMonth) * 100;
        }
        return variation;

    }


    public List<TopCategoryDTO> topCategories(String name, DateRangeType type) {

        UserEntity user = userService.findByUsername(name);
        DateRange fechas = DateRangeFactory.from(type);

        return expenseRepository.findTopCategories(user.getUsername(), fechas.from(), fechas.to());
    }




}
