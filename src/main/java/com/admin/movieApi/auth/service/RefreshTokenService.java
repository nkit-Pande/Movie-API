package com.admin.movieApi.auth.service;

import com.admin.movieApi.auth.entity.RefreshToken;
import com.admin.movieApi.auth.entity.User;
import com.admin.movieApi.auth.repository.RefreshTokenRepository;
import com.admin.movieApi.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken generateRefreshToken(String username) {
        User user = userRepository.findByEmail(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found"));

        RefreshToken refreshToken = user.getRefreshToken();

        if (refreshToken != null) {
            return refreshToken;
        } else {
            long refreshTokenExpiredAt = Instant.now().plusMillis(30*1000).toEpochMilli();
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .tokenExpiredAt(Instant.now().plusMillis(refreshTokenExpiredAt))
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);;
        }
        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        if(token.getTokenExpiredAt().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }
        return token;
    }

}
