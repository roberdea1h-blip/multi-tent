package com.multi.tent.pos_api.category;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multi.tent.pos_api.category.dto.CategoryDetailResponseDto;
import com.multi.tent.pos_api.category.dto.CategoryResponseDto;
import com.multi.tent.pos_api.category.dto.CreateCategoryDto;
import com.multi.tent.pos_api.category.dto.UpdateCategoryDto;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryValidator categoryValidator;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, CategoryValidator categoryValidator) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.categoryValidator = categoryValidator;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDetailResponseDto findById(UUID id) {
        Category category = categoryValidator.validateForExistence(id);
        return categoryMapper.toDetailResponseDto(category);
    }

    @Transactional
    public CategoryResponseDto create(CreateCategoryDto createDto) {
        categoryValidator.validateForCreation(createDto.getName());
        Category category = Category.create(createDto.getName());
        category = categoryRepository.save(category);
        return categoryMapper.toResponseDto(category);
    } 

    @Transactional
    public CategoryResponseDto update(UUID id, UpdateCategoryDto updateDto) {
        Category category = categoryValidator.validateForUpdate(id, updateDto.getName());
        category.setName(updateDto.getName());
        category = categoryRepository.save(category);
        return categoryMapper.toResponseDto(category);
    }

    @Transactional
    public void delete(UUID id) {
        Category category = categoryValidator.validateForExistence(id);
        categoryRepository.delete(category);
    }
}
