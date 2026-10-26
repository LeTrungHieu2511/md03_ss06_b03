package com.example.md03.repository;

import com.example.md03.dto.VehicleResponse;
import com.example.md03.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("""
            SELECT new com.example.md03.dto.VehicleResponse(
                v.id,
                v.licensePlate,
                v.color,
                v.type
            )
            FROM Vehicle v
            WHERE (CAST(:keyword AS string) IS NULL
                   OR LOWER(v.licensePlate) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')))
            """)
    Page<VehicleResponse> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}