package com.technical.workshop.service.impl;

import com.technical.workshop.exceptions.NotFoundException;
import com.technical.workshop.model.CarMaintenance;
import com.technical.workshop.model.User;
import com.technical.workshop.repositories.CarMaintenanceRepository;
import com.technical.workshop.repositories.ServiceCarRepository;
import com.technical.workshop.repositories.UserRepository;
import com.technical.workshop.service.CarMaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarMaintenanceServiceImpl implements CarMaintenanceService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceCarRepository serviceCarRepository;
    @Autowired
    private CarMaintenanceRepository carMaintenanceRepository;

    @Override
    public CarMaintenance findById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            var maintenanceId = user.get().getCarMaintenance().getId();
            Optional<CarMaintenance> carMaintenance = carMaintenanceRepository.findById(maintenanceId);
            if (carMaintenance.isPresent()) {
                return carMaintenance.get();
            }
        }
        throw new NotFoundException("Car not found");
    }
    
    @Override
    public CarMaintenance create(CarMaintenance carMaintenance) {
        return carMaintenanceRepository.insert(carMaintenance);
    }
//
    @Override
    public void delete(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            var serviceId = user.get().getServiceCar().getId();
            Optional<CarMaintenance> carMaintenance = carMaintenanceRepository.findById(serviceId);
            carMaintenance.ifPresent(car -> carMaintenanceRepository.delete(car));
        }
    }
    @Override
    public void update(CarMaintenance carMaintenance) {
        Optional<User> user = userRepository.findById(carMaintenance.getId());
        if (user.isPresent()){
        var maintenanceId = user.get().getCarMaintenance().getId();
            Optional<CarMaintenance> editedCarMaintenance = carMaintenanceRepository.findById(maintenanceId);
            if (editedCarMaintenance.isPresent()) {
                newData(editedCarMaintenance.get(), carMaintenance);
                carMaintenance.setId(maintenanceId);
                carMaintenanceRepository.save(editedCarMaintenance.get());
                return;
            }
        }
        throw new NotFoundException("User not found");
    }
    private void newData(CarMaintenance editedCarMaintenance, CarMaintenance carMaintenance) {
        editedCarMaintenance.setMaintenanceName(carMaintenance.getMaintenanceName());
    }
}
