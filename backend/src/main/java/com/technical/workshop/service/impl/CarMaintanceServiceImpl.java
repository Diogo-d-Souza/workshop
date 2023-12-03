package com.technical.workshop.service.impl;

import com.technical.workshop.exceptions.NotFoundException;
import com.technical.workshop.model.CarMaintenance;
import com.technical.workshop.model.User;
import com.technical.workshop.repositories.CarMaintanceRepository;
import com.technical.workshop.repositories.ServiceCarRepository;
import com.technical.workshop.repositories.UserRepository;
import com.technical.workshop.service.CarMaintanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarMaintanceServiceImpl implements CarMaintanceService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceCarRepository serviceCarRepository;
    @Autowired
    private CarMaintanceRepository carMaintanceRepository;

    @Override
    public CarMaintenance findById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            var maintenanceId = user.get().getCarMaintenance().getId();
            Optional<CarMaintenance> carMaintenance = carMaintanceRepository.findById(maintenanceId);
            if (carMaintenance.isPresent()) {
                return carMaintenance.get();
            }
        }
        throw new NotFoundException("Car not found");
    }
    
    @Override
    public CarMaintenance create(CarMaintenance carMaintenance) {
        return carMaintanceRepository.insert(carMaintenance);
    }
//
    @Override
    public void delete(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            var serviceId = user.get().getServiceCar().getId();
            Optional<CarMaintenance> carMaintenance = carMaintanceRepository.findById(serviceId);
            carMaintenance.ifPresent(car -> carMaintanceRepository.delete(car));
        }
    }
    @Override
    public void update(CarMaintenance carMaintenance) {
        Optional<User> user = userRepository.findById(carMaintenance.getId());
        if (user.isPresent()){
        var maintenanceId = user.get().getCarMaintenance().getId();
            Optional<CarMaintenance> editedCarMaintenance = carMaintanceRepository.findById(maintenanceId);
            if (editedCarMaintenance.isPresent()) {
                newData(editedCarMaintenance.get(), carMaintenance);
                carMaintenance.setId(maintenanceId);
                carMaintanceRepository.save(editedCarMaintenance.get());
                return;
            }
        }
        throw new NotFoundException("User not found");
    }
    private void newData(CarMaintenance editedCarMaintenance, CarMaintenance carMaintenance) {
        editedCarMaintenance.setMaintenanceName(carMaintenance.getMaintenanceName());
    }
}
