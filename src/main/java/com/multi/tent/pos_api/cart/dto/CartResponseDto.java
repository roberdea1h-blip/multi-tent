package com.multi.tent.pos_api.cart.dto;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Shopping cart with items and totals")
public class CartResponseDto {
    @Schema(description = "List of cart items")
    private List<CartItemResponseDto> items;

    @Schema(description = "Cart total", example = "150.00")
    private BigDecimal total;

    @Schema(description = "Total number of items (sum of quantities)", example = "5")
    private int totalItems;

    public List<CartItemResponseDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponseDto> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}
