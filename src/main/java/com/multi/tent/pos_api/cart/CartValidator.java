package com.multi.tent.pos_api.cart;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.multi.tent.pos_api.cart.model.Cart;
import com.multi.tent.pos_api.cart.model.CartItem;
import com.multi.tent.pos_api.common.exception.ResourceNotFoundException;

@Component
public class CartValidator {

    private final CartRepository cartRepository;

    public CartValidator(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartItem validateItemBelongsToCart(Cart cart, UUID itemId) {
        return cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));
    }
}
