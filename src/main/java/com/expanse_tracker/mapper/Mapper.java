package com.expanse_tracker.mapper;

import com.expanse_tracker.controller.dto.CategoryDTO;
import com.expanse_tracker.controller.dto.ExpenseRequestDTO;
import com.expanse_tracker.controller.dto.ExpenseResponseDTO;
import com.expanse_tracker.controller.dto.UserDTO;
import com.expanse_tracker.models.CategoryEntity;
import com.expanse_tracker.models.ExpenseEntity;
import com.expanse_tracker.models.UserEntity;


public class Mapper {

    public static UserDTO toDTO(UserEntity user) {
        if (user == null) return null;

        return UserDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public static ExpenseResponseDTO toDTO(ExpenseEntity expense) {
        if (expense == null) return null;

        return ExpenseResponseDTO.builder()
                .id(expense.getId())
                .description(expense.getDescription())
                .amount(expense.getAmount())
                .category(Mapper.toDTO(expense.getCategory()))
                .date(expense.getExpenseDate())
                .build();
    }


    public static UserEntity toEntity(UserDTO userRequest) {
        if (userRequest == null) return null;

        return UserEntity.builder()
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }



    public static ExpenseEntity toEntity(ExpenseRequestDTO dto) {
        if (dto == null) return null;

        return ExpenseEntity.builder()
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .expenseDate(dto.getDate())
                .build();
    }

    public static CategoryDTO toDTO(CategoryEntity category) {
        if (category == null) return null;

        return CategoryDTO.builder()
                .categoryName(category.getCategory().name())
                .color(category.getColor())
                .icon(category.getIcon())
                .background(category.getBackground())
                .build();
    }
}
