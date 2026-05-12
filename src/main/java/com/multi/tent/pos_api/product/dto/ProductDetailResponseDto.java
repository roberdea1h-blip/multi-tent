package com.multi.tent.pos_api.product.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detailed product information including description, images, and timestamps")
public class ProductDetailResponseDto {
    @Schema(description = "Product unique identifier", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Product name", example = "Coca Cola 500ml")
    private String name;

    @Schema(description = "Product description", example = "Refreshing carbonated soft drink")
    private String description;

    @Schema(description = "Product price", example = "25.00")
    private BigDecimal price;

    @Schema(description = "Current stock quantity", example = "100")
    private Integer stock;

    @Schema(description = "Minimum stock level", example = "10")
    private Integer minStock;

    @Schema(description = "Maximum stock level", example = "500")
    private Integer maxStock;

    @Schema(description = "Whether the product is active in catalog", example = "true")
    private boolean active;

    @Schema(description = "Category name", example = "Beverages")
    private String categoryName;

    @Schema(description = "Category ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID categoryId;

    @Schema(description = "Product images")
    private List<ProductImageResponseDto> images;

    @Schema(description = "Product creation timestamp")
    private Instant createdAt;

    @Schema(description = "Last update timestamp")
    private Instant updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getMinStock() {
        return minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }

    public Integer getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(Integer maxStock) {
        this.maxStock = maxStock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public List<ProductImageResponseDto> getImages() {
        return images;
    }

    public void setImages(List<ProductImageResponseDto> images) {
        this.images = images;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
