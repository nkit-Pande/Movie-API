package com.admin.movieApi.auth.service;


import com.admin.movieApi.auth.entity.User;
import com.admin.movieApi.auth.entity.UserRole;
import com.admin.movieApi.auth.repository.UserRepository;
import com.admin.movieApi.auth.utils.AuthResponse;
import com.admin.movieApi.auth.utils.LoginRequest;
import com.admin.movieApi.auth.utils.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;


    public AuthResponse register(
            RegisterRequest registerRequest
    ){
        var user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .build();

         User savedUser = userRepository.save(user);
         var accessToken = jwtService.generateToken(user);
         var refreshToken = refreshTokenService.generateRefreshToken(user.getEmail());

         return AuthResponse.builder()
                 .accessToken(accessToken)
                 .refreshToken(refreshToken.getRefreshToken())
                 .build();
    }

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.generateRefreshToken(user.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

}
