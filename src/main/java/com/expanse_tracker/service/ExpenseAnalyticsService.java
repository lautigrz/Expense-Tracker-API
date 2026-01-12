package com.expanse_tracker.service;

import com.expanse_tracker.controller.dto.ExpenseResponseDTO;
import com.expanse_tracker.controller.dto.TopCategoryDTO;
import com.expanse_tracker.enums.DateRangeType;
import com.expanse_tracker.mapper.Mapper;
import com.expanse_tracker.models.CategoryEntity;
import com.expanse_tracker.models.ExpenseEntity;
import com.expanse_tracker.models.UserEntity;
import com.expanse_tracker.records.DateRange;
import com.expanse_tracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
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

    public Double summary(String username, DateRangeType dateRangeType){
        DateRange fechas = getFecha(dateRangeType);
        return expenseRepository.sumByUserUsernameAndDateBetween(username, fechas.from(), fechas.to());
    }

    public List<ExpenseResponseDTO> filter(String username, DateRangeType dateRangeType) {
        UserEntity user = userService.findByUsername(username);

        DateRange fechas = getFecha(dateRangeType);
        List<ExpenseEntity> expenses = expenseRepository.findByUserAndExpenseDateBetween(user, fechas.from(), fechas.to());

        return expenses
                .stream()
                .map(Mapper::toDTO)
                .toList();

    }

    public List<ExpenseResponseDTO> filterCustom(String username, LocalDate start, LocalDate end) {

        UserEntity user = userService.findByUsername(username);

        List<ExpenseEntity> expenses = expenseRepository.findByUserAndExpenseDateBetween(user, start, end);

        return expenses
                .stream()
                .map(Mapper::toDTO)
                .toList();
    }

    public List<ExpenseResponseDTO> filterByCategory(String name, String category) {

        CategoryEntity categoryEntity = categoryService.findByCategory(category);

        return expenseRepository.findByUserUsernameAndCategory(name, categoryEntity)
                .stream()
                .map(Mapper::toDTO)
                .toList();
    }


    public Double getVaration(String username){
        DateRange thisMonth = getFecha(DateRangeType.THIS_MONTH);
        DateRange lastMonth = getFecha(DateRangeType.LAST_MONTH);

        Double totalThisMonth = expenseRepository.getTotalBetweenDates(username, thisMonth.from(), thisMonth.to());
        Double totalLastMonth = expenseRepository.getTotalBetweenDates(username, lastMonth.from(), lastMonth.to());
        System.out.println("Total This Month: " + totalThisMonth);
        System.out.println("Total Last Month: " + totalLastMonth);
        double variation = 0.0;

        if (totalLastMonth != 0) {
            variation = ((totalThisMonth - totalLastMonth) / totalLastMonth) * 100;
        }
        return variation;

    }

    private DateRange getFecha(DateRangeType type) {

        LocalDate today = LocalDate.now();

        return switch (type) {

            case THIS_WEEK -> {
                yield new DateRange(
                        today.with(DayOfWeek.MONDAY),
                        today
                );
            }

            case THIS_MONTH -> {
                yield new DateRange(
                        today.withDayOfMonth(1),
                        today
                );
            }

            case LAST_WEEK -> {
                LocalDate lastWeek = today.minusWeeks(1);
                yield new DateRange(
                        lastWeek.with(DayOfWeek.MONDAY),
                        lastWeek.with(DayOfWeek.SUNDAY)
                );
            }

            case LAST_MONTH -> {
                LocalDate lastMonth = today.minusMonths(1);
                yield new DateRange(
                        lastMonth.withDayOfMonth(1),
                        lastMonth.withDayOfMonth(lastMonth.lengthOfMonth())
                );
            }

            case LAST_3_MONTHS -> new DateRange(
                    today.minusMonths(3).withDayOfMonth(1),
                    today
            );
        };
    }

    public List<TopCategoryDTO> topCategories(String name, DateRangeType type) {

        UserEntity user = userService.findByUsername(name);
        DateRange fechas = getFecha(type);

        return expenseRepository.findTopCategories(user.getUsername(), fechas.from(), fechas.to());
    }
}
