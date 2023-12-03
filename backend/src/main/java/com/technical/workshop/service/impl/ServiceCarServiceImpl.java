package com.technical.workshop.service.impl;

import com.technical.workshop.exceptions.NotFoundException;
import com.technical.workshop.model.Car;
import com.technical.workshop.model.ServiceCar;
import com.technical.workshop.model.User;
import com.technical.workshop.repositories.CarRepository;
import com.technical.workshop.repositories.ServiceCarRepository;
import com.technical.workshop.repositories.UserRepository;
import com.technical.workshop.service.CarService;
import com.technical.workshop.service.ServiceCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceCarServiceImpl implements ServiceCarService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceCarRepository serviceCarRepository;

    @Override
    public ServiceCar findById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            var serviceCarId = user.get().getServiceCar().getId();
            Optional<ServiceCar> serviceCar = serviceCarRepository.findById(serviceCarId);
            if (serviceCar.isPresent()) {
                return serviceCar.get();
            }
        }
        throw new NotFoundException("Car not found");
    }

    @Override
    public ServiceCar create(ServiceCar serviceCar) {
        return serviceCarRepository.insert(serviceCar);
    }

    @Override
    public void delete(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            var serviceId = user.get().getServiceCar().getId();
            Optional<ServiceCar> serviceCar = serviceCarRepository.findById(serviceId);
            serviceCar.ifPresent(car -> serviceCarRepository.delete(car));
        }
    }

    //
    @Override
    public void update(ServiceCar serviceCar) {
        Optional<User> user = userRepository.findById(serviceCar.getId());
        if (user.isPresent()){
        var serviceId = user.get().getServiceCar().getId();
            Optional<ServiceCar> editedServiceCar = serviceCarRepository.findById(serviceId);
            if (editedServiceCar.isPresent()) {
                newData(editedServiceCar.get(), serviceCar);
                serviceCar.setId(serviceId);
                serviceCarRepository.save(editedServiceCar.get());
                return;
            }
        }
        throw new NotFoundException("User not found");
    }

    private void newData(ServiceCar editedServiceCar, ServiceCar serviceCar) {
        editedServiceCar.setServiceName(serviceCar.getServiceName());
    }

}
