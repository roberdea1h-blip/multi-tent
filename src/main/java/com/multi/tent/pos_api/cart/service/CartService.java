package com.multi.tent.pos_api.cart.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multi.tent.pos_api.auth.CustomUserDetails;
import com.multi.tent.pos_api.cart.CartMapper;
import com.multi.tent.pos_api.cart.CartRepository;
import com.multi.tent.pos_api.cart.CartValidator;
import com.multi.tent.pos_api.cart.dto.AddCartItemDto;
import com.multi.tent.pos_api.cart.dto.CartResponseDto;
import com.multi.tent.pos_api.cart.dto.UpdateCartItemDto;
import com.multi.tent.pos_api.cart.model.Cart;
import com.multi.tent.pos_api.cart.model.CartItem;
import com.multi.tent.pos_api.common.exception.ResourceNotFoundException;
import com.multi.tent.pos_api.product.ProductRepository;
import com.multi.tent.pos_api.product.model.Product;
import com.multi.tent.pos_api.user.User;
import com.multi.tent.pos_api.user.UserRepository;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartValidator cartValidator;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, CartMapper cartMapper,
                       CartValidator cartValidator, ProductRepository productRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.cartValidator = cartValidator;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public CartResponseDto getCart() {
        Cart cart = getOrCreateCart();
        return cartMapper.toResponseDto(cart);
    }

    public CartResponseDto addItem(AddCartItemDto dto) {
        Cart cart = getOrCreateCart();
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", dto.getProductId()));

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(dto.getProductId()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + dto.getQuantity());
        } else {
            CartItem item = CartItem.create(product, dto.getQuantity());
            cart.addItem(item);
        }

        cartRepository.save(cart);
        return cartMapper.toResponseDto(cart);
    }

    public CartResponseDto updateItem(UUID itemId, UpdateCartItemDto dto) {
        Cart cart = getOrCreateCart();
        CartItem item = cartValidator.validateItemBelongsToCart(cart, itemId);
        item.setQuantity(dto.getQuantity());
        cartRepository.save(cart);
        return cartMapper.toResponseDto(cart);
    }

    public void removeItem(UUID itemId) {
        Cart cart = getOrCreateCart();
        CartItem item = cartValidator.validateItemBelongsToCart(cart, itemId);
        cart.removeItem(item);
        cartRepository.save(cart);
    }

    public void clearCart() {
        Cart cart = getOrCreateCart();
        cart.clearItems();
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart() {
        User currentUser = getCurrentUser();
        return cartRepository.findByUserId(currentUser.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.create(currentUser);
                    return cartRepository.save(newCart);
                });
    }

    private User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId()));
    }
}
