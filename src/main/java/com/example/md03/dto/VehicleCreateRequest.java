package com.example.md03.dto;

import com.example.md03.model.VehicleType;

public class VehicleCreateRequest {
    private String licensePlate;
    private String color;
    private VehicleType vehicleType;

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
}