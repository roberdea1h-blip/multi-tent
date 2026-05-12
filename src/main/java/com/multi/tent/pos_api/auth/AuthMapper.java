package com.multi.tent.pos_api.auth;

import org.springframework.stereotype.Component;

import com.multi.tent.pos_api.auth.dto.AuthResponseDto;
import com.multi.tent.pos_api.user.User;

@Component
public class AuthMapper {
    public AuthResponseDto toAuthResponse(User user, String accessToken) {
        AuthResponseDto response = new AuthResponseDto();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setActive(user.isActive());
        response.setAccessToken(accessToken);
        return response;
    }

}
