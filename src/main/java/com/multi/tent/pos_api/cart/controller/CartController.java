package com.multi.tent.pos_api.cart.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.multi.tent.pos_api.cart.dto.AddCartItemDto;
import com.multi.tent.pos_api.cart.dto.CartResponseDto;
import com.multi.tent.pos_api.cart.dto.UpdateCartItemDto;
import com.multi.tent.pos_api.cart.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Cart", description = "Shopping cart operations")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @Operation(summary = "Get current user's cart",
               description = "Returns the authenticated user's shopping cart with items, totals, and quantities.")
    @ApiResponse(responseCode = "200", description = "Cart retrieved",
                 content = @Content(schema = @Schema(implementation = CartResponseDto.class)))
    public CartResponseDto getCart() {
        return cartService.getCart();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add item to cart",
               description = "Adds a product to the cart with the specified quantity. Requires USER role.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Item added to cart",
                     content = @Content(schema = @Schema(implementation = CartResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public CartResponseDto addItem(@RequestBody @Valid AddCartItemDto dto) {
        return cartService.addItem(dto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{itemId}")
    @Operation(summary = "Update cart item quantity",
               description = "Changes the quantity of an existing cart item. Requires USER role.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart item updated",
                     content = @Content(schema = @Schema(implementation = CartResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    public CartResponseDto updateItem(@PathVariable UUID itemId,
                                       @RequestBody @Valid UpdateCartItemDto dto) {
        return cartService.updateItem(itemId, dto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove item from cart",
               description = "Removes a specific item from the cart. Requires USER role.")
    @ApiResponse(responseCode = "204", description = "Cart item removed")
    public void removeItem(@PathVariable UUID itemId) {
        cartService.removeItem(itemId);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Clear cart",
               description = "Removes all items from the cart. Requires USER role.")
    @ApiResponse(responseCode = "204", description = "Cart cleared")
    public void clearCart() {
        cartService.clearCart();
    }
}
