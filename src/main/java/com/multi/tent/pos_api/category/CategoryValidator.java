package com.multi.tent.pos_api.category;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.multi.tent.pos_api.common.exception.DuplicateResourceException;
import com.multi.tent.pos_api.common.exception.ResourceNotFoundException;

@Component
public class CategoryValidator {
    private final CategoryRepository categoryRepository;

    public CategoryValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void validateForCreation(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new DuplicateResourceException("Category name", name);
        }
    }

    public Category validateForUpdate(UUID id, String name) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (!category.getName().equalsIgnoreCase(name) && categoryRepository.existsByName(name)) {
            throw new DuplicateResourceException("Category name", name);
        }

        return category;
    }

    public Category validateForExistence(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }
}
