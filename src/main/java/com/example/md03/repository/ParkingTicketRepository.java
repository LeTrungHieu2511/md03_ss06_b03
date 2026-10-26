package com.example.md03.repository;

import com.example.md03.model.ParkingTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, Long> {

    // Vé gần nhất của xe mà chưa check-out
    Optional<ParkingTicket> findFirstByVehicleIdAndCheckOutTimeIsNullOrderByCheckInTimeDesc(Long vehicleId);

    // Xe này đang ở trong bãi chưa
    boolean existsByVehicleIdAndCheckOutTimeIsNull(Long vehicleId);
}