package com.multi.tent.pos_api.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.multi.tent.pos_api.auth.dto.AuthResponseDto;
import com.multi.tent.pos_api.auth.dto.LoginRequestDto;
import com.multi.tent.pos_api.user.User;
import com.multi.tent.pos_api.user.UserRepository;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;    
    private final AuthMapper authMapper;

    public AuthService(
        JwtService jwtService, 
        UserRepository userRepository, 
        AuthMapper authMapper, 
        AuthenticationManager authenticationManager) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authMapper = authMapper;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getIdentifier())
                .or(() -> userRepository.findByUsername(request.getIdentifier()))
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword())
        );

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtService.generateAccessToken(userDetails);

        return authMapper.toAuthResponse(user, accessToken);
    }
    

}
