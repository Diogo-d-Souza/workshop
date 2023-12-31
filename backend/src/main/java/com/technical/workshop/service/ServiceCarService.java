package com.technical.workshop.service;

import com.technical.workshop.model.Car;
import com.technical.workshop.model.ServiceCar;
import org.springframework.stereotype.Service;

@Service
public interface ServiceCarService {
    public ServiceCar findById(String id);
    public ServiceCar create(ServiceCar serviceCar);
    public void delete(String id);
    public void update(ServiceCar serviceCar);
}
