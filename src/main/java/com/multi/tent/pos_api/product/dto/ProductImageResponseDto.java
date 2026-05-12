package com.multi.tent.pos_api.product.dto;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product image reference")
public class ProductImageResponseDto {
    @Schema(description = "Image unique identifier")
    private UUID id;

    @Schema(description = "Image URL", example = "https://example.com/images/product.jpg")
    private String imageUrl;

    @Schema(description = "External service public ID", example = "products/abc123")
    private String publicId;

    @Schema(description = "Image upload timestamp")
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
