package com.multi.tent.pos_api.product.model;

import com.multi.tent.pos_api.common.Entity.CreatableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_images")
public class ProductImage extends CreatableEntity {

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected ProductImage() {}

    private ProductImage(String imageUrl, String publicId) {
        this.imageUrl = imageUrl;
        this.publicId = publicId;
    }

    public static ProductImage create(String imageUrl, String publicId) {
        return new ProductImage(imageUrl, publicId);
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
