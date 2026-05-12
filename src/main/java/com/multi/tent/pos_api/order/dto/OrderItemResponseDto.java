package com.multi.tent.pos_api.order.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Item within an order")
public class OrderItemResponseDto {
    @Schema(description = "Product name", example = "Coca Cola 500ml")
    private String productName;

    @Schema(description = "Unit price at time of order", example = "25.00")
    private BigDecimal productPrice;

    @Schema(description = "Quantity ordered", example = "2")
    private int quantity;

    @Schema(description = "Subtotal (price × quantity)", example = "50.00")
    private BigDecimal subtotal;

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
}
