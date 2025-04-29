package com.example.login.signin.entity;

import com.example.login.signin.entity.Trip;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeName;
    private double latitude;
    private double longitude;

    @ManyToOne
    private Trip trip;
}
