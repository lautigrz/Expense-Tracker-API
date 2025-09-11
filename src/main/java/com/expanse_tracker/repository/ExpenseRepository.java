package com.expanse_tracker.repository;

import com.expanse_tracker.models.ExpenseEntity;
import com.expanse_tracker.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {
    List<ExpenseEntity> findByUserAndExpenseDateBetween(UserEntity user, LocalDate startDate, LocalDate endDate);
}
