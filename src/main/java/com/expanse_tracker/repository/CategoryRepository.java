package com.expanse_tracker.repository;

import com.expanse_tracker.models.CategoryEntity;
import com.expanse_tracker.models.ECategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    Optional<CategoryEntity> findByCategory(ECategory category);

}
