package com.multi.tent.pos_api.product.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multi.tent.pos_api.category.Category;
import com.multi.tent.pos_api.common.dto.PagedResponseDto;
import com.multi.tent.pos_api.product.ProductMapper;
import com.multi.tent.pos_api.product.ProductRepository;
import com.multi.tent.pos_api.product.ProductSpecification;
import com.multi.tent.pos_api.product.ProductValidator;
import com.multi.tent.pos_api.product.dto.CreateProductDto;
import com.multi.tent.pos_api.product.dto.CreateProductImageDto;
import com.multi.tent.pos_api.product.dto.ProductDetailResponseDto;
import com.multi.tent.pos_api.product.dto.ProductImageResponseDto;
import com.multi.tent.pos_api.product.dto.ProductResponseDto;
import com.multi.tent.pos_api.product.dto.UpdateProductDto;
import com.multi.tent.pos_api.product.model.Product;
import com.multi.tent.pos_api.product.model.ProductImage;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductValidator productValidator;

    public ProductService(ProductRepository productRepository,
                          ProductMapper productMapper,
                          ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productValidator = productValidator;
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ProductResponseDto> findAll(UUID categoryId, String search,
                                                         BigDecimal minPrice, BigDecimal maxPrice,
                                                         Pageable pageable) {
        Specification<Product> spec = Specification
                .where(ProductSpecification.isActive())
                .and(ProductSpecification.withCategory(categoryId))
                .and(ProductSpecification.withPriceBetween(minPrice, maxPrice))
                .and(ProductSpecification.withSearch(search));

        Page<Product> page = productRepository.findAll(spec, pageable);
        return PagedResponseDto.of(
                page.getContent().stream().map(productMapper::toResponseDto).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    @Transactional(readOnly = true)
    public ProductDetailResponseDto findById(UUID id) {
        Product product = productValidator.validateForExistence(id);
        return productMapper.toDetailResponseDto(product);
    }

    @Transactional
    public ProductResponseDto create(CreateProductDto createDto) {
        Category category = productValidator.validateCategory(createDto.getCategoryId());
        Product product = Product.create(
                createDto.getName(),
                createDto.getDescription(),
                createDto.getPrice(),
                createDto.getStock(),
                createDto.getMinStock(),
                createDto.getMaxStock(),
                category
        );
        product = productRepository.save(product);
        return productMapper.toResponseDto(product);
    }

    @Transactional
    public ProductResponseDto update(UUID id, UpdateProductDto updateDto) {
        Product product = productValidator.validateForExistence(id);
        Category category = productValidator.validateCategory(updateDto.getCategoryId());
        product.updateDetails(
                updateDto.getName(),
                updateDto.getDescription(),
                updateDto.getPrice(),
                updateDto.getStock(),
                updateDto.getMinStock(),
                updateDto.getMaxStock(),
                category
        );
        product = productRepository.save(product);
        return productMapper.toResponseDto(product);
    }

    @Transactional
    public void updateStock(UUID id, Integer newStock) {
        Product product = productValidator.validateForExistence(id);
        product.updateStock(newStock);
        productRepository.save(product);
    }

    @Transactional
    public void updateActive(UUID id, boolean active) {
        Product product = productValidator.validateForExistence(id);
        product.toggleActive(active);
        productRepository.save(product);
    }

    @Transactional
    public void delete(UUID id) {
        Product product = productValidator.validateForExistence(id);
        productRepository.delete(product);
    }

    @Transactional
    public ProductImageResponseDto addImage(UUID productId, CreateProductImageDto dto) {
        Product product = productValidator.validateForExistence(productId);
        ProductImage image = ProductImage.create(dto.getImageUrl(), dto.getPublicId());
        product.addImage(image);
        productRepository.save(product);
        return productMapper.toImageResponseDto(image);
    }

    @Transactional
    public void removeImage(UUID productId, UUID imageId) {
        ProductImage image = productValidator.validateImageForProduct(productId, imageId);
        image.getProduct().removeImage(image);
        productRepository.save(image.getProduct());
    }
}
