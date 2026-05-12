package com.multi.tent.pos_api.cart.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body to add an item to the cart")
public class AddCartItemDto {
    @NotNull(message = "Product ID is required")
    @Schema(description = "ID of the product to add", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Schema(description = "Quantity to add", example = "2")
    private Integer quantity;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
