package com.multi.tent.pos_api.product.dto;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body to create a new product")
public class CreateProductDto {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    @Schema(description = "Product name", example = "Coca Cola 500ml")
    private String name;

    @Size(max = 3000, message = "Description must not exceed 3000 characters")
    @Schema(description = "Product description", example = "Refreshing carbonated soft drink")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Schema(description = "Product price", example = "25.00")
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    @Schema(description = "Current stock quantity", example = "100")
    private Integer stock;

    @Min(value = 0, message = "Min stock cannot be negative")
    @Schema(description = "Minimum stock level for alerts", example = "10")
    private Integer minStock;

    @Min(value = 0, message = "Max stock cannot be negative")
    @Schema(description = "Maximum stock level", example = "500")
    private Integer maxStock;

    @NotNull(message = "Category is required")
    @Schema(description = "ID of the category this product belongs to", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID categoryId;

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

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }
}
