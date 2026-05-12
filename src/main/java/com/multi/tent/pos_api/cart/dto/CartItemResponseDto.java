package com.multi.tent.pos_api.cart.dto;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Item inside a shopping cart")
public class CartItemResponseDto {
    @Schema(description = "Cart item unique identifier")
    private UUID id;

    @Schema(description = "Product ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID productId;

    @Schema(description = "Product name", example = "Coca Cola 500ml")
    private String productName;

    @Schema(description = "Unit price", example = "25.00")
    private BigDecimal productPrice;

    @Schema(description = "Quantity in cart", example = "2")
    private int quantity;

    @Schema(description = "Subtotal (price × quantity)", example = "50.00")
    private BigDecimal subtotal;

    @Schema(description = "Product image URL")
    private String imageUrl;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
