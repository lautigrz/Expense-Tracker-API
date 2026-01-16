package com.expanse_tracker.service;

import com.expanse_tracker.controller.dto.CategoryDTO;
import com.expanse_tracker.exception.InvalidCategoryException;
import com.expanse_tracker.mapper.Mapper;
import com.expanse_tracker.models.CategoryEntity;
import com.expanse_tracker.models.ECategory;
import com.expanse_tracker.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

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

    public List<CategoryDTO> getAllCategories() {

        Iterable<CategoryEntity> categories = categoryRepository.findAll();

        return StreamSupport.stream(categories.spliterator(),false)
                .map(Mapper::toDTO)
                .toList();

    }


}
