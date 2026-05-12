package com.multi.tent.pos_api.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body to update an order's status")
public class UpdateOrderStatusDto {
    @NotNull(message = "Status is required")
    @Schema(description = "New order status", example = "CONFIRMED",
            allowableValues = {"PENDING", "CONFIRMED", "PREPARING", "READY", "DELIVERED", "CANCELLED"})
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
