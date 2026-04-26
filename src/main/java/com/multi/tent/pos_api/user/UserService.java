package com.multi.tent.pos_api.user;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.multi.tent.pos_api.common.exception.ResourceNotFoundException;
import com.multi.tent.pos_api.user.dto.CreateUserDto;
import com.multi.tent.pos_api.user.dto.UserResponseDto;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final UserMapper userMapper;

    public UserService(
        UserRepository userRepository, 
        PasswordEncoder passwordEncoder, 
        UserValidator userValidator, 
        UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
        this.userMapper = userMapper;
    }

    public UserResponseDto register(CreateUserDto userDto) {
        userValidator.validateForCreation(userDto);

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        
        User user = User.create(
                userDto.getEmail(),
                userDto.getUsername(),
                encodedPassword,
                UserRole.EMPLOYEE
        );

        userRepository.save(user);

        return userMapper.toResponseDto(user);
    }

    public void updateName(UUID userId, String newUsername) {
        User user = getUserById(userId);

        userValidator.validateUsername(newUsername);
        user.updateUsername(newUsername);
        userRepository.save(user);
    }

    public void updateEmail(UUID userId, String newEmail) {
        User user = getUserById(userId);
        userValidator.validateEmail(newEmail);
        user.updateEmail(newEmail);
        userRepository.save(user);
    }

    public UserResponseDto me(UUID userId) {
        User user = getUserById(userId);
        return userMapper.toResponseDto(user);
    }


    // Helper methods
    private User getUserById(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
    }

}
