package com.example.demo.controller;

import com.example.demo.document.Vehicle;
import com.example.demo.dto.SearchRequest;
import com.example.demo.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicle")
public class VehicleController {

  private final VehicleService vehicleService;

  @PostMapping
  public ResponseEntity<Void> create(
      @RequestBody Vehicle vehicle
  ) {
    vehicleService.create(vehicle);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Vehicle> findById(
      @PathVariable String id
  ) {
    return ResponseEntity.ok(vehicleService.findById(id));
  }

  @PostMapping("/search")
  public ResponseEntity<Set<Vehicle>> searchVehicle(
      @RequestBody SearchRequest request
  ) {
    Set<Vehicle> vehicles = vehicleService.searchForVehicle(request);

    return ResponseEntity.ok(vehicles);
  }

}
