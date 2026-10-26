package com.example.md03.service;

import com.example.md03.dto.PageResponse;
import com.example.md03.dto.VehicleCreateRequest;
import com.example.md03.dto.VehicleResponse;
import com.example.md03.model.Vehicle;
import com.example.md03.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public VehicleResponse createVehicle(VehicleCreateRequest req) {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(req.getLicensePlate());
        vehicle.setColor(req.getColor());
        vehicle.setType(req.getVehicleType());
        Vehicle saved = vehicleRepository.save(vehicle);
        return new VehicleResponse(saved.getId(), saved.getLicensePlate(),
                saved.getColor(), saved.getType());
    }

    public PageResponse<VehicleResponse> getPagedVehicles(int page, int size, String sortBy,
                                                          String direction, String keyword) {
        if (page < 0) page = 0;
        if (size < 1) size = 10;

        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isBlank() && direction != null && !direction.isBlank()) {
            sort = Sort.Direction.fromOptionalString(direction.trim())
                    .map(dir -> Sort.by(dir, sortBy.trim()))
                    .orElse(Sort.unsorted());
        }

        String kw = (keyword == null || keyword.isBlank()) ? null : keyword.trim();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<VehicleResponse> result = vehicleRepository.findAllByKeyword(kw, pageable);

        return new PageResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast()
        );
    }
}