package com.multi.tent.pos_api.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.multi.tent.pos_api.user.User;
import com.multi.tent.pos_api.user.UserRole;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    private User createUser(String email, String username, String password, UserRole role) {
        User user = User.create(email, username, password, role);
        user.setId(UUID.randomUUID());
        return user;
    }

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey",
                "dGhpcyBpcyBhIHNlY3JldCBrZXkgZm9yIEpXVCB0ZXN0cyBwdXJwb3Nlcy4=");
        ReflectionTestUtils.setField(jwtService, "accessExpiration", 86400000L);
    }

    @Test
    void generateAccessToken_ShouldCreateTokenWithCorrectClaims() {
        User user = createUser("test@test.com", "testuser", "password", UserRole.USER);
        CustomUserDetails userDetails = new CustomUserDetails(user);

        String token = jwtService.generateAccessToken(userDetails);

        assertThat(token).isNotNull().isNotBlank();
        assertThat(jwtService.extractUserId(token)).isEqualTo(user.getId());
        assertThat(jwtService.extractUsername(token)).isEqualTo("testuser");
        assertThat(jwtService.extractRole(token)).isEqualTo("ROLE_USER");
    }

    @Test
    void generateAccessToken_WithAdminRole_ShouldContainAdminRole() {
        User user = createUser("admin@test.com", "adminuser", "password", UserRole.ADMIN);
        CustomUserDetails userDetails = new CustomUserDetails(user);

        String token = jwtService.generateAccessToken(userDetails);

        assertThat(jwtService.extractRole(token)).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void isTokenValid_WithMatchingUser_ShouldReturnTrue() {
        User user = createUser("valid@test.com", "validuser", "password", UserRole.USER);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String token = jwtService.generateAccessToken(userDetails);

        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertThat(valid).isTrue();
    }

    @Test
    void isTokenValid_WithDifferentUser_ShouldReturnFalse() {
        User user1 = createUser("user1@test.com", "user1", "password", UserRole.USER);
        User user2 = createUser("user2@test.com", "user2", "password", UserRole.USER);
        CustomUserDetails details1 = new CustomUserDetails(user1);
        CustomUserDetails details2 = new CustomUserDetails(user2);
        String token = jwtService.generateAccessToken(details1);

        boolean valid = jwtService.isTokenValid(token, details2);

        assertThat(valid).isFalse();
    }

    @Test
    void extractUserId_ShouldReturnCorrectUuid() {
        User user = createUser("uuid@test.com", "uuiduser", "password", UserRole.USER);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String token = jwtService.generateAccessToken(userDetails);

        UUID extracted = jwtService.extractUserId(token);

        assertThat(extracted).isEqualTo(user.getId());
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        User user = createUser("name@test.com", "cooluser", "password", UserRole.USER);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        String token = jwtService.generateAccessToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("cooluser");
    }
}
