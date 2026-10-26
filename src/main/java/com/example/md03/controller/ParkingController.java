package com.example.md03.controller;

import com.example.md03.dto.ApiResponse;
import com.example.md03.dto.TicketRequest;
import com.example.md03.dto.TicketResponse;
import com.example.md03.service.ParkingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tickets")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<ApiResponse<TicketResponse>> checkIn(@RequestBody TicketRequest req) {
        TicketResponse result = parkingService.checkIn(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Check-in successfully", result));
    }

    @PutMapping("/check-out/{vehicleId}")
    public ResponseEntity<ApiResponse<TicketResponse>> checkOut(@PathVariable Long vehicleId) {
        TicketResponse result = parkingService.checkOut(vehicleId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Check-out successfully", result));
    }
}