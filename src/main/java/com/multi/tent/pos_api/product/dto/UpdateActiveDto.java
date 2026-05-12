package com.multi.tent.pos_api.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body to toggle a product's active status")
public class UpdateActiveDto {
    @NotNull(message = "Active is required")
    @Schema(description = "Whether the product is active (visible in catalog)", example = "true")
    private Boolean active;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
