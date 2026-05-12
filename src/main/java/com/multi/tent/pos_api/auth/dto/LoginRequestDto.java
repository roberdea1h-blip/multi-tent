package com.multi.tent.pos_api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request — email or username in the identifier field")
public class LoginRequestDto {

    @NotBlank(message = "Username or email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Email (or username, though @Email validation currently blocks plain usernames)",
            example = "user@example.com")
    private String identifier;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "password123")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
