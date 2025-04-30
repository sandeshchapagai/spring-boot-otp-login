package com.example.login.signin.controller;

import com.example.login.signin.entity.Users;
import com.example.login.signin.repository.UserRepository;
import com.example.login.signin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7);
            Users user = (Users) userService.getUserByToken(token);
            return ResponseEntity.ok(user); // Return user details
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or user not found");
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editUser(@PathVariable Long id, @RequestBody Users updatedUser) {
        Optional<Users> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            Users user = existingUser.get();

            if (updatedUser.getName() != null) {
                user.setName(updatedUser.getName());
            }
            if (updatedUser.getEmail() != null) {
                user.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getUsername() != null) {
                user.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getPassword() != null) {
                user.setPassword(updatedUser.getPassword());
            }

            Users savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
