package com.multi.tent.pos_api.cart.model;

import java.util.ArrayList;
import java.util.List;

import com.multi.tent.pos_api.common.Entity.AuditableEntity;
import com.multi.tent.pos_api.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carts")
public class Cart extends AuditableEntity {

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CartItem> items = new ArrayList<>();

    protected Cart() {}

    private Cart(User user) {
        this.user = user;
    }

    public static Cart create(User user) {
        return new Cart(user);
    }

    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
    }

    public void clearItems() {
        items.clear();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
