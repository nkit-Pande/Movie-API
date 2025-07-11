package com.admin.movieApi.controller;

import com.admin.movieApi.auth.entity.ForgotPassword;
import com.admin.movieApi.auth.entity.User;
import com.admin.movieApi.auth.repository.ForgetPasswordRepository;
import com.admin.movieApi.auth.repository.UserRepository;
import com.admin.movieApi.auth.utils.ChangePassword;
import com.admin.movieApi.dto.MailBody;
import com.admin.movieApi.service.Email.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/forget-password")
public class ForgetPasswordController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgetPasswordRepository forgetPasswordRepository;
    private final PasswordEncoder passwordEncoder;


    public ForgetPasswordController(UserRepository userRepository, EmailService emailService, ForgetPasswordRepository forgetPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgetPasswordRepository = forgetPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Working";
    }


    @Transactional
    @PostMapping("/verify-email/{mail}")
    public ResponseEntity<String> verifyEmail(@PathVariable String mail) {

        User user = userRepository.findByEmail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + mail));
        
        if (user.getForgotPassword() != null) {
            forgetPasswordRepository.delete(user.getForgotPassword());
            user.setForgotPassword(null);
            userRepository.saveAndFlush(user);
        }
        

        int otp = generateOTP();
        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .user(user)
                .build();
        
        fp = forgetPasswordRepository.saveAndFlush(fp);
        
        user.setForgotPassword(fp);
        userRepository.saveAndFlush(user);
        
        MailBody mailBody = MailBody.builder()
                .to(mail)
                .subject("OTP for Forget Password Request")
                .text("This is the OTP for email verification: " + otp + "\nThis OTP will expire in 1 minute.")
                .build();
                
        emailService.sendMail(mailBody);
        
        return ResponseEntity.ok("Email sent for verification! Please check your inbox for the OTP.");
    }

    @PostMapping("/verify-otp/{otp}/{email}")
    public ResponseEntity<String> verifyOTP(
            @PathVariable Integer otp,
            @PathVariable String email
    ) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found with email: " + email));
        ForgotPassword fp = forgetPasswordRepository.findByOtpAndEmail(otp, email).orElseThrow(() -> new RuntimeException("Invalid OTP"));
        if(fp.getExpirationTime().before(new Date())) {
            forgetPasswordRepository.deleteById(fp.getFpid());
            return new ResponseEntity<>("OTP expired!", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>("OTP verified successfully!", HttpStatus.OK);
    }

    @PostMapping("/reset-password/{email}")
    public ResponseEntity<String> resetPassword(
            @RequestBody ChangePassword changePassword,
            @PathVariable String email
    ){
            if(!Objects.equals(changePassword.password(), changePassword.newPassword())) {
                return new ResponseEntity<>("Passwords do not match!", HttpStatus.EXPECTATION_FAILED);
            }
            String encodedPassword = passwordEncoder.encode(changePassword.newPassword());
            userRepository.updatePassword(email, encodedPassword);

            return ResponseEntity.ok("Password changed successfully!");
    }

    private Integer generateOTP() {
        Random random = new Random();
        return random.nextInt(100_000,999_999);
    }

}
