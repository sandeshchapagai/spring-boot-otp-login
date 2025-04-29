package com.example.login.signin.entity;

import com.example.login.signin.entity.Trip;
import com.example.login.signin.entity.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String imageUrl;
    private String createdAt;

    @ManyToOne
    private Users creator;

    @ManyToOne
    private Trip trip;
}
