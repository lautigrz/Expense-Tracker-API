package com.expanse_tracker.service;

import com.expanse_tracker.controller.dto.ExpenseRequest;
import com.expanse_tracker.controller.dto.ExpenseResponse;
import com.expanse_tracker.models.CategoryEntity;
import com.expanse_tracker.models.ECategory;
import com.expanse_tracker.models.ExpenseEntity;
import com.expanse_tracker.models.UserEntity;
import com.expanse_tracker.repository.ExpenseRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    public ExpenseService(ExpenseRepository expenseRepository, UserService userService, CategoryService categoryService) {
        this.categoryService = categoryService;
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }


    @Transactional
    public ExpenseResponse addExpense(ExpenseRequest expense) {

        UserEntity user = userService.findById(expense.getUserId());

        ExpenseEntity expenseEntity = ExpenseEntity.builder()
                .description(expense.getDescription())
                .amount(expense.getAmount())
                .category(categoryService.findByCategory(expense.getCategory()))
                .user(user)
                .build();

        user.getExpenses().add(expenseEntity);
      //  expenseRepository.save(expenseEntity);


        return expenseResponse(user.getUsername(), expenseEntity);

    }


    @Transactional
    public void deleteUserExpense(String name, Long expenseId) {
        UserEntity user = userService.findByUsername(name);

        ExpenseEntity expenseEntity = user.getExpenses()
                .stream()
                .filter(expense -> expense.getId().equals(expenseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        user.getExpenses().remove(expenseEntity);

        //expenseRepository.delete(expenseEntity);
    }

    public ExpenseResponse updateUserExpense(String name, Long expenseId, ExpenseRequest expenseRequest) {
        UserEntity user = userService.findByUsername(name);

        ExpenseEntity expenseEntity = user.getExpenses()
                .stream()
                .filter(expense -> expense.getId().equals(expenseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        expenseEntity.setDescription(expenseRequest.getDescription());
        expenseEntity.setAmount(expenseRequest.getAmount());
        CategoryEntity category = categoryService.findByCategory(expenseRequest.getCategory());
        expenseEntity.setCategory(category);
        expenseEntity.setExpenseDate(LocalDate.now());
        expenseRepository.save(expenseEntity);

        return expenseResponse(user.getUsername(), expenseEntity);
    }


    public List<ExpenseResponse> filter(String username,Integer idFilter){
        UserEntity user = userService.findByUsername(username);
        LocalDate today = LocalDate.now();
        List<ExpenseEntity> expenses = new ArrayList<>();
        switch (idFilter){
            case 1: // Semana pasada
                LocalDate startOfLastWeek = today.minusWeeks(1).with(DayOfWeek.MONDAY);
                LocalDate endOfLastWeek = today.minusWeeks(1).with(DayOfWeek.SUNDAY);
                expenses = expenseRepository.findByUserAndExpenseDateBetween(user, startOfLastWeek, endOfLastWeek);
                break;
            case 2: // Mes pasado
                LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
                LocalDate lastDayOfLastMonth = today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth());
                expenses = expenseRepository.findByUserAndExpenseDateBetween(user, firstDayOfLastMonth, lastDayOfLastMonth);
                break;

            case 3: // Últimos 3 meses
                LocalDate threeMonthsAgo = today.minusMonths(3).withDayOfMonth(1);
                expenses = expenseRepository.findByUserAndExpenseDateBetween(user, threeMonthsAgo, today);
                break;
        }


        List<ExpenseResponse> expenseResponses = expenses
                .stream()
                .map(exponse -> expenseResponse(user.getUsername(), exponse))
                .toList();

        return expenseResponses;

    }


    private ExpenseResponse expenseResponse(String username, ExpenseEntity expenseEntity) {
        return ExpenseResponse.builder()
                .username(username)
                .description(expenseEntity.getDescription())
                .amount(expenseEntity.getAmount())
                .category(expenseEntity.getCategory().getCategory().name())
                .build();
    }
}
