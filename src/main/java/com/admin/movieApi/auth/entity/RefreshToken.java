package com.admin.movieApi.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.time.Instant;

@Entity
@Builder
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    @Column(nullable = false,length = 500)
    @NotBlank(message = "Refresh token cannot be empty")
    private String refreshToken;
    @Column(nullable = false)
    private Instant tokenExpiredAt;
    @OneToOne
    private User user;

//    public RefreshToken(Integer tokenId, String refreshToken, Instant tokenExpiredAt, User user) {
//        this.tokenId = tokenId;
//        this.refreshToken = refreshToken;
//        this.tokenExpiredAt = tokenExpiredAt;
//        this.user = user;
//    }
//    public RefreshToken(){}


}
