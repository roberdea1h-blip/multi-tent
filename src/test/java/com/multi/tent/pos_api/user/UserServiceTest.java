package com.multi.tent.pos_api.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.multi.tent.pos_api.common.exception.DuplicateResourceException;
import com.multi.tent.pos_api.common.exception.ResourceNotFoundException;
import com.multi.tent.pos_api.user.dto.CreateUserDto;
import com.multi.tent.pos_api.user.dto.UserResponseDto;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserValidator userValidator;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void register_ShouldEncodePasswordAndSaveUser() {
        CreateUserDto dto = new CreateUserDto();
        dto.setEmail("test@test.com");
        dto.setUsername("testuser");
        dto.setPassword("rawPassword");

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDto responseDto = new UserResponseDto();
        when(userMapper.toResponseDto(any())).thenReturn(responseDto);

        UserResponseDto result = userService.register(dto);

        assertThat(result).isSameAs(responseDto);
        verify(userValidator).validateForCreation(dto);
        verify(userRepository).save(userCaptor.capture());

        User saved = userCaptor.getValue();
        assertThat(saved.getEmail()).isEqualTo("test@test.com");
        assertThat(saved.getUsername()).isEqualTo("testuser");
        assertThat(saved.getPassword()).isEqualTo("encodedPassword");
        assertThat(saved.getRole()).isEqualTo(UserRole.USER);
        assertThat(saved.isActive()).isTrue();
    }

    @Test
    void register_WhenEmailAlreadyExists_ShouldThrowDuplicateResourceException() {
        CreateUserDto dto = new CreateUserDto();
        dto.setEmail("dup@test.com");
        dto.setUsername("dupuser");
        dto.setPassword("password123");

        doThrow(new DuplicateResourceException("Email", "dup@test.com"))
                .when(userValidator).validateForCreation(dto);

        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("dup@test.com");

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void register_WhenUsernameAlreadyExists_ShouldThrowDuplicateResourceException() {
        CreateUserDto dto = new CreateUserDto();
        dto.setEmail("unique@test.com");
        dto.setUsername("dupuser");
        dto.setPassword("password123");

        doThrow(new DuplicateResourceException("Username", "dupuser"))
                .when(userValidator).validateForCreation(dto);

        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("dupuser");

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateName_ShouldUpdateUsername() {
        UUID userId = UUID.randomUUID();
        String newUsername = "newuser";

        User existingUser = User.create("test@test.com", "olduser", "encoded", UserRole.USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.updateName(userId, newUsername);

        assertThat(existingUser.getUsername()).isEqualTo("newuser");
        verify(userValidator).validateUsername("newuser");
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateName_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateName(userId, "newuser"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User");

        verify(userValidator, never()).validateUsername(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateName_WhenUsernameAlreadyExists_ShouldThrowDuplicateResourceException() {
        UUID userId = UUID.randomUUID();
        User existingUser = User.create("test@test.com", "olduser", "encoded", UserRole.USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        doThrow(new DuplicateResourceException("Username", "taken"))
                .when(userValidator).validateUsername("taken");

        assertThatThrownBy(() -> userService.updateName(userId, "taken"))
                .isInstanceOf(DuplicateResourceException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateEmail_ShouldUpdateEmail() {
        UUID userId = UUID.randomUUID();
        String newEmail = "new@test.com";

        User existingUser = User.create("old@test.com", "user", "encoded", UserRole.USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.updateEmail(userId, newEmail);

        assertThat(existingUser.getEmail()).isEqualTo("new@test.com");
        verify(userValidator).validateEmail("new@test.com");
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateEmail_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateEmail(userId, "new@test.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User");

        verify(userValidator, never()).validateEmail(any());
        verify(userRepository, never()).save(any());
    }
}
