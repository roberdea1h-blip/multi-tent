package com.multi.tent.pos_api.cart;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.multi.tent.pos_api.cart.model.Cart;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserId(UUID userId);
}
