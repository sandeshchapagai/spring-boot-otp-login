package com.example.login.signin.controller;

import com.example.login.signin.entity.Users;
import com.example.login.signin.exception.OTPExpiredException;
import com.example.login.signin.payload.ForgotPasswordRequest;
import com.example.login.signin.payload.LoginRequest;
import com.example.login.signin.payload.ResetPasswordRequest;
import com.example.login.signin.repository.UserRepository;
import com.example.login.signin.service.CustomUserDetailsService;
import com.example.login.signin.service.JwtService;
import com.example.login.signin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        try {
            if (userRepository.findByUsername(user.getUsername()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already taken");
            }
            if (userRepository.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already registered");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Users savedUser = userRepository.save(user);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(savedUser.getUsername());
            String token = jwtService.generateToken(userDetails);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            Users user = userRepository.findByUsername(loginRequest.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            userService.sendForgotPasswordEmail(request.getEmail());
            return ResponseEntity.ok("Password reset email sent successfully");
        } catch (OTPExpiredException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        try {
            userService.verifyOTP(email, otp); // Implement this method in your UserService
            return ResponseEntity.ok("OTP verified successfully");
        } catch (OTPExpiredException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OTP verification failed: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            userService.resetPassword(request);
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}
