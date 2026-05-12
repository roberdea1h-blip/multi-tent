package com.multi.tent.pos_api.auth.dto;

import com.multi.tent.pos_api.user.dto.UserResponseDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response with JWT token")
public class AuthResponseDto extends UserResponseDto {
    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIs...")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
