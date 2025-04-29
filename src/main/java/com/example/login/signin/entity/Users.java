package com.example.login.signin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String otp;

    @Column
    private LocalDateTime otpExpiry;

    @Column(nullable = false)
    private String name;

    // 👇 Add these mappings for Trip Mager
    @OneToMany(mappedBy = "user")
    private List<UserTrip> userTrips;

    @OneToMany(mappedBy = "creator")
    private List<Expense> expenses;

    @OneToMany(mappedBy = "creator")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Reminder> reminders;
}
