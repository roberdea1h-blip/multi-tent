package com.multi.tent.pos_api.order;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.multi.tent.pos_api.order.dto.OrderDetailResponseDto;
import com.multi.tent.pos_api.order.dto.OrderItemResponseDto;
import com.multi.tent.pos_api.order.dto.OrderResponseDto;
import com.multi.tent.pos_api.order.model.Order;
import com.multi.tent.pos_api.order.model.OrderItem;

@Component
public class OrderMapper {

    public OrderResponseDto toResponseDto(Order order) {
        if (order == null) return null;
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus().name());
        dto.setTotal(order.getTotal());
        dto.setItemsCount(order.getItems().size());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }

    public OrderDetailResponseDto toDetailResponseDto(Order order) {
        if (order == null) return null;
        OrderDetailResponseDto dto = new OrderDetailResponseDto();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus().name());
        dto.setTotal(order.getTotal());
        dto.setItems(toItemResponseDtoList(order.getItems()));
        dto.setUserId(order.getUser().getId());
        dto.setUsername(order.getUser().getUsername());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }

    public OrderItemResponseDto toItemResponseDto(OrderItem item) {
        if (item == null) return null;
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setProductName(item.getProductName());
        dto.setProductPrice(item.getProductPrice());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }

    private List<OrderItemResponseDto> toItemResponseDtoList(List<OrderItem> items) {
        if (items == null) return Collections.emptyList();
        return items.stream()
                .map(this::toItemResponseDto)
                .collect(Collectors.toList());
    }
}
