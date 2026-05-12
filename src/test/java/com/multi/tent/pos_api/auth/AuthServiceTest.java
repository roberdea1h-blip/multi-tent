package com.multi.tent.pos_api.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.multi.tent.pos_api.auth.dto.AuthResponseDto;
import com.multi.tent.pos_api.auth.dto.LoginRequestDto;
import com.multi.tent.pos_api.user.User;
import com.multi.tent.pos_api.user.UserRepository;
import com.multi.tent.pos_api.user.UserRole;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthMapper authMapper;

    @InjectMocks
    private AuthService authService;

    @Captor
    private ArgumentCaptor<UsernamePasswordAuthenticationToken> authCaptor;

    @Test
    void login_WithEmail_ShouldAuthenticateAndReturnResponse() {
        LoginRequestDto request = new LoginRequestDto();
        request.setIdentifier("user@test.com");
        request.setPassword("password123");

        User user = User.create("user@test.com", "testuser", "encoded", UserRole.USER);
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("user@test.com")).thenReturn(Optional.empty());

        String token = "jwt-token-value";
        when(jwtService.generateAccessToken(any())).thenReturn(token);

        AuthResponseDto expected = new AuthResponseDto();
        expected.setAccessToken(token);
        when(authMapper.toAuthResponse(user, token)).thenReturn(expected);

        AuthResponseDto result = authService.login(request);

        assertThat(result).isSameAs(expected);
        assertThat(result.getAccessToken()).isEqualTo(token);
        verify(authenticationManager).authenticate(authCaptor.capture());
        assertThat(authCaptor.getValue().getPrincipal()).isEqualTo("testuser");
        assertThat(authCaptor.getValue().getCredentials()).isEqualTo("password123");
    }

    @Test
    void login_WithUsername_ShouldAuthenticateAndReturnResponse() {
        LoginRequestDto request = new LoginRequestDto();
        request.setIdentifier("testuser");
        request.setPassword("password123");

        User user = User.create("user@test.com", "testuser", "encoded", UserRole.USER);
        when(userRepository.findByEmail("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        String token = "jwt-token-value";
        when(jwtService.generateAccessToken(any())).thenReturn(token);

        AuthResponseDto expected = new AuthResponseDto();
        expected.setAccessToken(token);
        when(authMapper.toAuthResponse(user, token)).thenReturn(expected);

        AuthResponseDto result = authService.login(request);

        assertThat(result.getAccessToken()).isEqualTo(token);
        verify(authenticationManager).authenticate(authCaptor.capture());
        assertThat(authCaptor.getValue().getPrincipal()).isEqualTo("testuser");
    }

    @Test
    void login_WithInvalidCredentials_ShouldThrowUsernameNotFoundException() {
        LoginRequestDto request = new LoginRequestDto();
        request.setIdentifier("unknown@test.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("unknown@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Invalid credentials");

        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateAccessToken(any());
    }

    @Test
    void login_WhenAuthenticationFails_ShouldPropagateException() {
        LoginRequestDto request = new LoginRequestDto();
        request.setIdentifier("user@test.com");
        request.setPassword("wrongpassword");

        User user = User.create("user@test.com", "testuser", "encoded", UserRole.USER);
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("user@test.com")).thenReturn(Optional.empty());

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Bad credentials");

        verify(jwtService, never()).generateAccessToken(any());
    }
}
