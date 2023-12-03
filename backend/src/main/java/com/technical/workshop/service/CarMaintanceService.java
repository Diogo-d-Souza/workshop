package com.technical.workshop.service;

import com.technical.workshop.model.CarMaintenance;
import org.springframework.stereotype.Service;

@Service
public interface CarMaintanceService {
    public CarMaintenance findById(String id);
    public CarMaintenance create(CarMaintenance carMaintenance);
    public void delete(String id);
    public void update(CarMaintenance carMaintenance);
}
