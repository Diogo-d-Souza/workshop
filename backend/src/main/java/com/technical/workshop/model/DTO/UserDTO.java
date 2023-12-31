package com.technical.workshop.model.DTO;

import com.technical.workshop.model.Car;
import com.technical.workshop.model.CarMaintenance;
import com.technical.workshop.model.ServiceCar;
import com.technical.workshop.model.User;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    @DBRef
    private Car car;
    private ServiceCar serviceCar;
    private CarMaintenance carMaintenance;

    public UserDTO(User user) {
        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        password = user.getPassword();
        car = user.getCar();
        serviceCar = user.getServiceCar();
        carMaintenance = user.getCarMaintenance();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public ServiceCar getServiceCar() {
        return serviceCar;
    }

    public void setServiceCar(ServiceCar serviceCar) {
        this.serviceCar = serviceCar;
    }

    public CarMaintenance getCarMaintenance() {
        return carMaintenance;
    }

    public void setCarMaintenance(CarMaintenance carMaintenance) {
        this.carMaintenance = carMaintenance;
    }
}
