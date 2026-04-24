package com.multi.tent.pos_api.user;

import org.springframework.stereotype.Component;

import com.multi.tent.pos_api.user.dto.UserDetailResponseDto;
import com.multi.tent.pos_api.user.dto.UserResponseDto;

@Component
public class UserMapper {
     public UserResponseDto toResponseDto(User user) {
        if (user == null) return null;
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());
        dto.setActive(user.isActive());
        return dto;
    }
    public UserDetailResponseDto toDetailResponseDto(User user) {
        if (user == null) return null;

        UserDetailResponseDto dto = new UserDetailResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());
        dto.setActive(user.isActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }
}
