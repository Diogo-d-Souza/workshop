package com.technical.workshop.service.impl;

import com.technical.workshop.exceptions.NotFoundException;
import com.technical.workshop.model.Car;
import com.technical.workshop.repositories.CarRepository;
import com.technical.workshop.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceCarServiceImpl implements CarService {
    @Autowired
    private CarRepository carRepository;

    @Override
    public Car findById(String id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()){
            return car.get();
        }
        throw new NotFoundException("Car not found");
    }

    @Override
    public Car create(Car car) {
        return carRepository.insert(car);
    }

    @Override
    public void delete(String id){
        carRepository.delete(findById(id));
    }

    @Override
    public void update(Car car){
        Optional<Car> editedCar = carRepository.findById(car.getId());
        if(editedCar.isPresent()){
            newData(editedCar.get(), car);
            carRepository.save(editedCar.get());
            return;
        }
        throw new NotFoundException("User not found");
    }

    private void newData(Car editedCar, Car car) {
        editedCar.setBrand(car.getBrand());
        editedCar.setYear(car.getYear());
        editedCar.setLicensePlate(car.getLicensePlate());
    }

}
