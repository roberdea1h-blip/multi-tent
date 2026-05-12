package com.multi.tent.pos_api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body to update user profile (email and/or username)")
public class UpdateUserDto {
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(description = "New email address", example = "updated@example.com")
    private String email;

    @Size(min = 4, max = 30, message = "Username must be between 4 and 30 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9._]+$",
        message = "Username can only contain letters, numbers, dots and underscores"
    )
    @Schema(description = "New username", example = "updated.user")
    private String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
