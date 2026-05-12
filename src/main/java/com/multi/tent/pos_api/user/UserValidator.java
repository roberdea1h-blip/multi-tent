package com.multi.tent.pos_api.user;

import org.springframework.stereotype.Component;

import com.multi.tent.pos_api.common.exception.DuplicateResourceException;
import com.multi.tent.pos_api.user.dto.CreateUserDto;

@Component
public class UserValidator {
    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateForCreation(CreateUserDto userDto) {

        validateEmail(userDto.getEmail());
        validateUsername(userDto.getUsername());
    }

    public void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email", email);
        }
    }

    public void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username", username);
        }
    }
}
