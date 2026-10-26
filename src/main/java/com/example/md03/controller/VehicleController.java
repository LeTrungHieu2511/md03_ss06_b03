package com.example.md03.controller;

import com.example.md03.dto.ApiResponse;
import com.example.md03.dto.PageResponse;
import com.example.md03.dto.VehicleCreateRequest;
import com.example.md03.dto.VehicleResponse;
import com.example.md03.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VehicleResponse>> create(@RequestBody VehicleCreateRequest req) {
        VehicleResponse created = vehicleService.createVehicle(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Vehicle created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<VehicleResponse>>> getVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String keyword) {

        PageResponse<VehicleResponse> result =
                vehicleService.getPagedVehicles(page, size, sortBy, direction, keyword);

        return ResponseEntity.ok(new ApiResponse<>(true, "Vehicles fetched successfully", result));
    }
}