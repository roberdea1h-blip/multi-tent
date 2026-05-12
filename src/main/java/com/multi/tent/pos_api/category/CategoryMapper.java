package com.multi.tent.pos_api.category;

import org.springframework.stereotype.Component;

import com.multi.tent.pos_api.category.dto.CategoryDetailResponseDto;
import com.multi.tent.pos_api.category.dto.CategoryResponseDto;

@Component
public class CategoryMapper {
    public CategoryResponseDto toResponseDto(Category category) {
        if (category == null) return null;
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    public CategoryDetailResponseDto toDetailResponseDto(Category category) {
        if (category == null) return null;
        CategoryDetailResponseDto dto = new CategoryDetailResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCreatedAt(category.getCreatedAt());
        return dto;
    }
}
