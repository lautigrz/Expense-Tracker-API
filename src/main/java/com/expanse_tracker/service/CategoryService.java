package com.expanse_tracker.service;

import com.expanse_tracker.exception.InvalidCategoryException;
import com.expanse_tracker.models.CategoryEntity;
import com.expanse_tracker.models.ECategory;
import com.expanse_tracker.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryRepository.save(category);
    }

    public CategoryEntity findByCategory(String category) {
        try {
            ECategory enumCategory = ECategory.valueOf(category.toUpperCase());
            return categoryRepository.findByCategory(enumCategory)
                    .orElseGet(() -> createCategory(CategoryEntity.builder()
                            .category(enumCategory)
                            .build()
                    ));
        } catch (IllegalArgumentException e) {
            throw new InvalidCategoryException("Categoría inválida: " + category);
        }
    }


}
