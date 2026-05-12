package com.multi.tent.pos_api.product;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.multi.tent.pos_api.category.Category;
import com.multi.tent.pos_api.category.CategoryRepository;
import com.multi.tent.pos_api.common.exception.ResourceNotFoundException;
import com.multi.tent.pos_api.product.model.Product;
import com.multi.tent.pos_api.product.model.ProductImage;

@Component
public class ProductValidator {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductValidator(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product validateForExistence(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    public Category validateCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
    }

    public ProductImage validateImageForProduct(UUID productId, UUID imageId) {
        Product product = validateForExistence(productId);
        return product.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", imageId));
    }
}
