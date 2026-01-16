package com.expanse_tracker.repository;

import com.expanse_tracker.controller.dto.TopCategoryDTO;
import com.expanse_tracker.models.CategoryEntity;
import com.expanse_tracker.models.ExpenseEntity;
import com.expanse_tracker.models.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {
    List<ExpenseEntity> findByUserAndExpenseDateBetween(UserEntity user, LocalDate startDate, LocalDate endDate);
    List<ExpenseEntity> findByUserUsernameAndCategory(String username, CategoryEntity category);
    List<ExpenseEntity> findByUserUsername(String username, Sort sort);
    @Query("""
    SELECT SUM(e.amount)
    FROM ExpenseEntity e
    WHERE e.user.username = :username
      AND e.expenseDate >= :startDate
      AND e.expenseDate <= :endDate
""")
    Double sumByUserUsernameAndDateBetween(
            @Param("username") String username,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
    SELECT new com.expanse_tracker.controller.dto.TopCategoryDTO(
        e.category.category,
        e.category.color,
        SUM(e.amount)
       )
       FROM ExpenseEntity e
       WHERE e.user.username  = :username
       AND e.expenseDate >= :startDate
       AND e.expenseDate <= :endDate
       GROUP BY e.category.category, e.category.color
       ORDER BY SUM(e.amount) DESC
           
  """)
    List<TopCategoryDTO> findTopCategories(@Param("username") String username, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query(""" 
    SELECT COALESCE(SUM(e.amount), 0)
    FROM ExpenseEntity e
    WHERE e.user.username = :username
      AND e.expenseDate >= :startDate
      AND e.expenseDate <= :endDate
""")
    Double getTotalBetweenDates(@Param("username") String username, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
