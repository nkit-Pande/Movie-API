package com.admin.movieApi.auth.repository;

import com.admin.movieApi.auth.entity.ForgotPassword;
import com.admin.movieApi.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgetPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.otp = ?1 AND fp.user.email = ?2")
    Optional<ForgotPassword> findByOtpAndEmail(Integer otp, String email);
    
    Optional<ForgotPassword> findByUser(User user);

}
