package com.example.md03.service;

import com.example.md03.dto.TicketRequest;
import com.example.md03.dto.TicketResponse;
import com.example.md03.model.ParkingTicket;
import com.example.md03.model.Vehicle;
import com.example.md03.model.Zone;
import com.example.md03.repository.ParkingTicketRepository;
import com.example.md03.repository.VehicleRepository;
import com.example.md03.repository.ZoneRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class ParkingService {

    private final ParkingTicketRepository ticketRepository;
    private final VehicleRepository vehicleRepository;
    private final ZoneRepository zoneRepository;

    public ParkingService(ParkingTicketRepository ticketRepository,
                          VehicleRepository vehicleRepository,
                          ZoneRepository zoneRepository) {
        this.ticketRepository = ticketRepository;
        this.vehicleRepository = vehicleRepository;
        this.zoneRepository = zoneRepository;
    }

    @Transactional
    public TicketResponse checkIn(TicketRequest req) {
        if (req.getVehicleId() == null || req.getZoneId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vehicleId và zoneId là bắt buộc");
        }

        Vehicle vehicle = vehicleRepository.findById(req.getVehicleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy xe với id: " + req.getVehicleId()));

        Zone zone = zoneRepository.findById(req.getZoneId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy khu vực với id: " + req.getZoneId()));

        if (ticketRepository.existsByVehicleIdAndCheckOutTimeIsNull(vehicle.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Xe này đang ở trong bãi");
        }

        if (zone.getOccupiedSpots() >= zone.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Khu vực " + zone.getName() + " đã hết chỗ");
        }

        ParkingTicket ticket = new ParkingTicket();
        ticket.setVehicle(vehicle);
        ticket.setZone(zone);
        ticket.setCheckInTime(LocalDateTime.now());
        ParkingTicket saved = ticketRepository.save(ticket);

        zone.setOccupiedSpots(zone.getOccupiedSpots() + 1);
        zoneRepository.save(zone);

        return toResponse(saved);
    }

    @Transactional
    public TicketResponse checkOut(Long vehicleId) {
        ParkingTicket ticket = ticketRepository
                .findFirstByVehicleIdAndCheckOutTimeIsNullOrderByCheckInTimeDesc(vehicleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Xe id " + vehicleId + " không có vé đang gửi"));

        ticket.setCheckOutTime(LocalDateTime.now());
        ParkingTicket saved = ticketRepository.save(ticket);

        Zone zone = saved.getZone();
        zone.setOccupiedSpots(Math.max(0, zone.getOccupiedSpots() - 1));
        zoneRepository.save(zone);

        return toResponse(saved);
    }

    // Phải gọi bên trong transaction vì vehicle và zone là LAZY
    private TicketResponse toResponse(ParkingTicket t) {
        return new TicketResponse(
                t.getId(),
                t.getVehicle().getLicensePlate(),
                t.getZone().getName(),
                t.getCheckInTime(),
                t.getCheckOutTime()
        );
    }
}