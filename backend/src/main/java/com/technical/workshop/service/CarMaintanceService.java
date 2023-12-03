package com.technical.workshop.service;

import com.technical.workshop.model.CarMaintance;
import com.technical.workshop.model.ServiceCar;
import org.springframework.stereotype.Service;

@Service
public interface CarMaintanceService {
    //    public ServiceCar findById(String id);
    public CarMaintance create(CarMaintance carMaintance);
//    public void delete(String id);
//    public void update(ServiceCar serviceCar);
}
