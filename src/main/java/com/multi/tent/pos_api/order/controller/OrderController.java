package com.multi.tent.pos_api.order.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.multi.tent.pos_api.common.dto.PagedResponseDto;
import com.multi.tent.pos_api.order.OrderStatus;
import com.multi.tent.pos_api.order.dto.OrderDetailResponseDto;
import com.multi.tent.pos_api.order.dto.OrderResponseDto;
import com.multi.tent.pos_api.order.dto.UpdateOrderStatusDto;
import com.multi.tent.pos_api.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Order management — create, list, and update orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an order",
               description = "Converts the current user's cart into an order. Requires authentication.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Order created",
                     content = @Content(schema = @Schema(implementation = OrderDetailResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Cart is empty or invalid")
    })
    public OrderDetailResponseDto create() {
        return orderService.createOrder();
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user's orders",
               description = "Returns paginated orders for the authenticated user. Requires authentication.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Paginated list of user's orders",
                 content = @Content(schema = @Schema(implementation = PagedResponseDto.class)))
    public PagedResponseDto<OrderResponseDto> getMyOrders(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return orderService.getMyOrders(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID",
               description = "Returns detailed order information. Users can only see their own orders. Requires authentication.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order found",
                     content = @Content(schema = @Schema(implementation = OrderDetailResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public OrderDetailResponseDto getOrderById(@PathVariable UUID id) {
        return orderService.getOrderById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "List all orders (Admin)",
               description = "Returns paginated orders for all users. Requires ADMIN role.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Paginated list of all orders",
                 content = @Content(schema = @Schema(implementation = PagedResponseDto.class)))
    public PagedResponseDto<OrderResponseDto> getAllOrders(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status (Admin)",
               description = "Changes the status of an order. Requires ADMIN role.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order status updated"),
        @ApiResponse(responseCode = "400", description = "Invalid status value"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public void updateStatus(@PathVariable UUID id,
                             @RequestBody @Valid UpdateOrderStatusDto dto) {
        OrderStatus status = OrderStatus.valueOf(dto.getStatus());
        orderService.updateStatus(id, status);
    }
}
