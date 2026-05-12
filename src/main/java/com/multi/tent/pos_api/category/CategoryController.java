package com.multi.tent.pos_api.category;

import java.util.List;
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

import com.multi.tent.pos_api.category.dto.CategoryDetailResponseDto;
import com.multi.tent.pos_api.category.dto.CategoryResponseDto;
import com.multi.tent.pos_api.category.dto.CreateCategoryDto;
import com.multi.tent.pos_api.category.dto.UpdateCategoryDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "Product category management")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "List all categories",
               description = "Returns all categories. Public endpoint.")
    @ApiResponse(responseCode = "200", description = "List of categories",
                 content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDto.class))))
    public List<CategoryResponseDto> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID",
               description = "Returns a single category with timestamps. Public endpoint.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category found",
                     content = @Content(schema = @Schema(implementation = CategoryDetailResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public CategoryDetailResponseDto findById(@PathVariable UUID id) {
        return categoryService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a category",
               description = "Creates a new category. Requires ADMIN role.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Category created",
                     content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "409", description = "Category name already exists")
    })
    public CategoryResponseDto create(@RequestBody @Valid CreateCategoryDto createDto) {
        return categoryService.create(createDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a category",
               description = "Updates the name of an existing category. Requires ADMIN role.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category updated",
                     content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "409", description = "Category name already exists")
    })
    public CategoryResponseDto update(@PathVariable UUID id, @RequestBody @Valid UpdateCategoryDto updateDto) {
        return categoryService.update(id, updateDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category",
               description = "Deletes a category by ID. Requires ADMIN role.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Category deleted"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public void delete(@PathVariable UUID id) {
        categoryService.delete(id);
    }
}
