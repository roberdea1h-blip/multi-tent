package com.multi.tent.pos_api.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUsernameDto {
    
    @NotBlank
    @Size(min = 4, max = 30, message = "Username must be between 4 and 30 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9._]+$",
        message = "Username can only contain letters, numbers, dots and underscores"
    )
    private String username;

    public String getUsername() {
        return username;
    }
    
}
