package com.multi.tent.pos_api.cart.model;

import java.math.BigDecimal;

import com.multi.tent.pos_api.common.Entity.BaseEntity;
import com.multi.tent.pos_api.product.model.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_items")
public class CartItem extends BaseEntity {

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected CartItem() {}

    private CartItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static CartItem create(Product product, Integer quantity) {
        return new CartItem(product, quantity);
    }

    public BigDecimal getSubtotal() {
        return product.getPrice()
                .multiply(BigDecimal.valueOf(quantity));
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
