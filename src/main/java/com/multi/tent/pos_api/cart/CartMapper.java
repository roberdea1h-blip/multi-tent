package com.multi.tent.pos_api.cart;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.multi.tent.pos_api.cart.dto.CartItemResponseDto;
import com.multi.tent.pos_api.cart.dto.CartResponseDto;
import com.multi.tent.pos_api.cart.model.Cart;
import com.multi.tent.pos_api.cart.model.CartItem;

@Component
public class CartMapper {

    public CartResponseDto toResponseDto(Cart cart) {
        if (cart == null) return null;
        CartResponseDto dto = new CartResponseDto();
        dto.setItems(toItemResponseDtoList(cart.getItems()));
        dto.setTotal(cart.getItems().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setTotalItems(cart.getItems().size());
        return dto;
    }

    public CartItemResponseDto toItemResponseDto(CartItem item) {
        if (item == null) return null;
        CartItemResponseDto dto = new CartItemResponseDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductPrice(item.getProduct().getPrice());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getSubtotal());
        if (item.getProduct().getImages() != null && !item.getProduct().getImages().isEmpty()) {
            dto.setImageUrl(item.getProduct().getImages().get(0).getImageUrl());
        }
        return dto;
    }

    private List<CartItemResponseDto> toItemResponseDtoList(List<CartItem> items) {
        if (items == null) return Collections.emptyList();
        return items.stream()
                .map(this::toItemResponseDto)
                .collect(Collectors.toList());
    }
}
