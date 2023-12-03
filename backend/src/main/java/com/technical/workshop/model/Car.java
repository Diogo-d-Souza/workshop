package com.technical.workshop.model;

import com.technical.workshop.model.DTO.CarOwnerDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Car {
    @Id
    private String id;
    private String brand;
    private String licensePlate;
    private Integer year;
    private CarOwnerDTO user;
    private ServiceCar serviceCar;

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

    public ServiceCar getServiceCar() {
        return serviceCar;
    }

    public void setServiceCar(ServiceCar serviceCar) {
        this.serviceCar = serviceCar;
    }
}
