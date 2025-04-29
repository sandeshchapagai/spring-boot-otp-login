package com.example.login.signin.controller;

import com.example.login.signin.entity.Trip;
import com.example.login.signin.repository.TripRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/trips")
public class TripController {
    @Autowired
    private TripRepository tripRepository;

    @GetMapping("/all")
public ResponseEntity<List<Trip>> getAllTrips(){
     List<Trip> trips = tripRepository.findAll();
     return ResponseEntity.ok(trips);
    }

    @PostMapping("/create")
    public ResponseEntity<Trip> createTrip(@Valid @RequestBody Trip trip) {
        Trip savedTrip = tripRepository.save(trip);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrip);
    }

}
