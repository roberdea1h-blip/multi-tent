package com.multi.tent.pos_api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body to update the username")
public class UpdateUsernameDto {

    @NotBlank
    @Size(min = 4, max = 30, message = "Username must be between 4 and 30 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9._]+$",
        message = "Username can only contain letters, numbers, dots and underscores"
    )
    @Schema(description = "New username", example = "new.username")
    private String username;

    public String getUsername() {
        return username;
    }

}
