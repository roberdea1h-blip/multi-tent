package com.multi.tent.pos_api.product;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.multi.tent.pos_api.product.dto.ProductDetailResponseDto;
import com.multi.tent.pos_api.product.dto.ProductImageResponseDto;
import com.multi.tent.pos_api.product.dto.ProductResponseDto;
import com.multi.tent.pos_api.product.model.Product;
import com.multi.tent.pos_api.product.model.ProductImage;

@Component
public class ProductMapper {

    public ProductResponseDto toResponseDto(Product product) {
        if (product == null) return null;
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setActive(product.isActive());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            dto.setMainImageUrl(product.getImages().get(0).getImageUrl());
        }
        return dto;
    }

    public ProductDetailResponseDto toDetailResponseDto(Product product) {
        if (product == null) return null;
        ProductDetailResponseDto dto = new ProductDetailResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setMinStock(product.getMinStock());
        dto.setMaxStock(product.getMaxStock());
        dto.setActive(product.isActive());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }
        dto.setImages(toImageResponseDtoList(product.getImages()));
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }

    public ProductImageResponseDto toImageResponseDto(ProductImage image) {
        if (image == null) return null;
        ProductImageResponseDto dto = new ProductImageResponseDto();
        dto.setId(image.getId());
        dto.setImageUrl(image.getImageUrl());
        dto.setPublicId(image.getPublicId());
        dto.setCreatedAt(image.getCreatedAt());
        return dto;
    }

    public List<ProductImageResponseDto> toImageResponseDtoList(List<ProductImage> images) {
        if (images == null) return Collections.emptyList();
        return images.stream()
                .map(this::toImageResponseDto)
                .collect(Collectors.toList());
    }
}
