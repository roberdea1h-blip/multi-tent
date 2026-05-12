package com.multi.tent.pos_api.order.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multi.tent.pos_api.auth.CustomUserDetails;
import com.multi.tent.pos_api.cart.CartRepository;
import com.multi.tent.pos_api.cart.model.Cart;
import com.multi.tent.pos_api.cart.model.CartItem;
import com.multi.tent.pos_api.common.dto.PagedResponseDto;
import com.multi.tent.pos_api.common.exception.BusinessRuleException;
import com.multi.tent.pos_api.common.exception.ForbiddenException;
import com.multi.tent.pos_api.common.exception.ResourceNotFoundException;
import com.multi.tent.pos_api.order.OrderMapper;
import com.multi.tent.pos_api.order.OrderRepository;
import com.multi.tent.pos_api.order.OrderStatus;
import com.multi.tent.pos_api.order.OrderValidator;
import com.multi.tent.pos_api.order.dto.OrderDetailResponseDto;
import com.multi.tent.pos_api.order.dto.OrderResponseDto;
import com.multi.tent.pos_api.order.model.Order;
import com.multi.tent.pos_api.order.model.OrderItem;
import com.multi.tent.pos_api.product.ProductRepository;
import com.multi.tent.pos_api.product.model.Product;
import com.multi.tent.pos_api.user.User;
import com.multi.tent.pos_api.user.UserRepository;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderValidator orderValidator;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper,
                        OrderValidator orderValidator, CartRepository cartRepository,
                        ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderValidator = orderValidator;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public OrderDetailResponseDto createOrder() {
        User currentUser = getCurrentUser();
        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new BusinessRuleException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new BusinessRuleException("Cart is empty");
        }

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BusinessRuleException(
                        "Insufficient stock for product '" + product.getName()
                        + "'. Available: " + product.getStock() + ", requested: " + cartItem.getQuantity());
            }
        }

        Order order = Order.create(currentUser, java.math.BigDecimal.ZERO);
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            product.updateStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.create(product, cartItem.getQuantity());
            order.addItem(orderItem);
            total = total.add(orderItem.getSubtotal());
        }

        order.setTotal(total);
        order = orderRepository.save(order);

        cart.clearItems();
        cartRepository.save(cart);

        return orderMapper.toDetailResponseDto(order);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<OrderResponseDto> getMyOrders(Pageable pageable) {
        User currentUser = getCurrentUser();
        Page<Order> page = orderRepository.findByUserId(currentUser.getId(), pageable);
        return PagedResponseDto.of(
                page.getContent().stream().map(orderMapper::toResponseDto).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    @Transactional(readOnly = true)
    public OrderDetailResponseDto getOrderById(UUID id) {
        User currentUser = getCurrentUser();
        Order order = orderValidator.validateForExistence(id);

        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        if (!isAdmin && !order.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You can only view your own orders");
        }

        return orderMapper.toDetailResponseDto(order);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<OrderResponseDto> getAllOrders(Pageable pageable) {
        Page<Order> page = orderRepository.findAll(pageable);
        return PagedResponseDto.of(
                page.getContent().stream().map(orderMapper::toResponseDto).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    public void updateStatus(UUID id, OrderStatus newStatus) {
        Order order = orderValidator.validateForExistence(id);
        orderValidator.validateStatusTransition(order, newStatus);
        order.updateStatus(newStatus);
        orderRepository.save(order);
    }

    private User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId()));
    }
}
