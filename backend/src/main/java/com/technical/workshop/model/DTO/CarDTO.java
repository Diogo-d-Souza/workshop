package com.technical.workshop.model.DTO;

import com.technical.workshop.model.Car;
import com.technical.workshop.model.User;

import java.util.Date;

public class CarDTO {
    private String id;
    private String brand;
    private String licensePlate;
    private Integer year;
    private CarOwnerDTO user;

    public CarDTO(Car car) {
        id = car.getId();
        brand = car.getBrand();
        licensePlate = car.getLicensePlate();
        year = car.getYear();
        user = car.getUser();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public CarOwnerDTO getUser() {
        return user;
    }

    public void setUser(CarOwnerDTO user) {
        this.user = user;
    }
}
