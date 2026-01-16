package com.expanse_tracker.service;

import com.expanse_tracker.controller.dto.ExpenseRequestDTO;
import com.expanse_tracker.controller.dto.ExpenseResponseDTO;
import com.expanse_tracker.exception.ExpenseNotFoundException;
import com.expanse_tracker.mapper.Mapper;
import com.expanse_tracker.models.CategoryEntity;
import com.expanse_tracker.models.ExpenseEntity;
import com.expanse_tracker.models.UserEntity;
import com.expanse_tracker.repository.ExpenseRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.stream;

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
    public ExpenseResponseDTO addExpense(String username, ExpenseRequestDTO expense) {

        UserEntity user = userService.findByUsername(username);

        ExpenseEntity expenseEntity = Mapper.toEntity(expense);
        expenseEntity.setUser(user);

        CategoryEntity category = categoryService.findByCategory(expense.getCategory());
        expenseEntity.setCategory(category);

        LocalDate date = expense.getDate() != null ? expense.getDate() : LocalDate.now();
        expenseEntity.setExpenseDate(date);

        return Mapper.toDTO(expenseRepository.save(expenseEntity));

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

       expenseRepository.delete(expenseEntity);
    }

    public ExpenseResponseDTO updateUserExpense(String name, Long expenseId, ExpenseRequestDTO expenseRequest) {
        UserEntity user = userService.findByUsername(name);

        ExpenseEntity expenseEntity = user.getExpenses()
                .stream()
                .filter(expense -> expense.getId().equals(expenseId))
                .findFirst()
                .orElseThrow(() -> new ExpenseNotFoundException("Expense no encontrado"));

        expenseEntity.setDescription(expenseRequest.getDescription());
        expenseEntity.setAmount(expenseRequest.getAmount());
        CategoryEntity category = categoryService.findByCategory(expenseRequest.getCategory());
        expenseEntity.setCategory(category);
        expenseEntity.setExpenseDate(LocalDate.now());
        expenseRepository.save(expenseEntity);

        return Mapper.toDTO(expenseRepository.save(expenseEntity));
    }


    public List<ExpenseResponseDTO> getAllExpenses(String name) {

        UserEntity user = userService.findByUsername(name);
        Sort sort = Sort.by(Sort.Direction.DESC, "expenseDate");
        List<ExpenseEntity> expenses = expenseRepository.findByUserUsername(user.getUsername(),sort);
        return expenses.stream()
                .map(Mapper::toDTO)
                .toList();

    }
}