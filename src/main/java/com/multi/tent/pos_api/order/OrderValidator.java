package com.multi.tent.pos_api.order;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.multi.tent.pos_api.common.exception.BusinessRuleException;
import com.multi.tent.pos_api.common.exception.ResourceNotFoundException;
import com.multi.tent.pos_api.order.model.Order;

@Component
public class OrderValidator {

    private final OrderRepository orderRepository;

    public OrderValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order validateForExistence(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    public void validateStatusTransition(Order order, OrderStatus newStatus) {
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessRuleException("Cannot update a cancelled order");
        }
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BusinessRuleException("Cannot update a delivered order");
        }
    }
}
