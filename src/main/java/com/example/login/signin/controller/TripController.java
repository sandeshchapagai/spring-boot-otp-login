package com.example.login.signin.controller;

import com.example.login.signin.entity.Trip;
import com.example.login.signin.model.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<Trip>>> getAllTrips() {
        List<Trip> trips = tripRepository.findAll();
        return ResponseEntity.ok(
                new ApiResponse<>("Trips retrieved successfully", trips)
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Trip>> createTrip(@Valid @RequestBody Trip trip) {
        Trip savedTrip = tripRepository.save(trip);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>("Trip created successfully", savedTrip)
        );
    }

    // Example for error case (if needed)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Trip>> getTripById(@PathVariable Long id) {
        return tripRepository.findById(id)
                .map(trip -> ResponseEntity.ok(
                        new ApiResponse<>("Trip found", trip)
                ))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ApiResponse<>("Trip not found", null)
                ));
    }
}
