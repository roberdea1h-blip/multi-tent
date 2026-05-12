package com.multi.tent.pos_api.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body to add an image to a product")
public class CreateProductImageDto {
    @NotBlank(message = "Image URL is required")
    @Schema(description = "URL of the product image", example = "https://example.com/images/product.jpg")
    private String imageUrl;

    @NotBlank(message = "Public ID is required")
    @Schema(description = "Cloudinary or external service public ID for the image", example = "products/abc123")
    private String publicId;

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
}
