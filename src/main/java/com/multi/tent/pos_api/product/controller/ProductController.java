package com.multi.tent.pos_api.product.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.multi.tent.pos_api.common.dto.PagedResponseDto;
import com.multi.tent.pos_api.product.dto.CreateProductDto;
import com.multi.tent.pos_api.product.dto.CreateProductImageDto;
import com.multi.tent.pos_api.product.dto.ProductDetailResponseDto;
import com.multi.tent.pos_api.product.dto.ProductImageResponseDto;
import com.multi.tent.pos_api.product.dto.ProductResponseDto;
import com.multi.tent.pos_api.product.dto.UpdateActiveDto;
import com.multi.tent.pos_api.product.dto.UpdateProductDto;
import com.multi.tent.pos_api.product.dto.UpdateStockDto;
import com.multi.tent.pos_api.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Product catalog management")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "List products",
               description = "Returns paginated products with optional filters. Public endpoint.")
    @ApiResponse(responseCode = "200", description = "Paginated list of products")
    public PagedResponseDto<ProductResponseDto> findAll(
            @Parameter(description = "Filter by category ID")
            @RequestParam(required = false) UUID category,
            @Parameter(description = "Search by name or description")
            @RequestParam(required = false) String search,
            @Parameter(description = "Minimum price filter")
            @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price filter")
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return productService.findAll(category, search, minPrice, maxPrice, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID",
               description = "Returns detailed product information including images. Public endpoint.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product found",
                     content = @Content(schema = @Schema(implementation = ProductDetailResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ProductDetailResponseDto findById(@PathVariable UUID id) {
        return productService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a product",
               description = "Creates a new product. Requires ADMIN role.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Product created",
                     content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ProductResponseDto create(@RequestBody @Valid CreateProductDto createDto) {
        return productService.create(createDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a product",
               description = "Updates all fields of an existing product. Requires ADMIN role.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product updated",
                     content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "404", description = "Product or category not found")
    })
    public ProductResponseDto update(@PathVariable UUID id,
                                      @RequestBody @Valid UpdateProductDto updateDto) {
        return productService.update(id, updateDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update product stock",
               description = "Partially updates only the stock quantity of a product. Requires ADMIN role.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock updated"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public void updateStock(@PathVariable UUID id,
                            @RequestBody @Valid UpdateStockDto updateDto) {
        productService.updateStock(id, updateDto.getStock());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/active")
    @Operation(summary = "Toggle product active status",
               description = "Activates or deactivates a product. Requires ADMIN role.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Active status updated"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public void updateActive(@PathVariable UUID id,
                             @RequestBody @Valid UpdateActiveDto updateDto) {
        productService.updateActive(id, updateDto.getActive());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a product",
               description = "Deletes a product by ID. Requires ADMIN role.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Product deleted"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public void delete(@PathVariable UUID id) {
        productService.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{productId}/images")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add product image",
               description = "Adds an image URL to a product. Requires ADMIN role.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Image added",
                     content = @Content(schema = @Schema(implementation = ProductImageResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ProductImageResponseDto addImage(@PathVariable UUID productId,
                                              @RequestBody @Valid CreateProductImageDto dto) {
        return productService.addImage(productId, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}/images/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove product image",
               description = "Removes an image from a product. Requires ADMIN role.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Image removed"),
        @ApiResponse(responseCode = "404", description = "Product or image not found")
    })
    public void removeImage(@PathVariable UUID productId, @PathVariable UUID imageId) {
        productService.removeImage(productId, imageId);
    }
}
