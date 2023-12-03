package com.technical.workshop.service;

import com.technical.workshop.model.Car;
import com.technical.workshop.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CarService {
    public Car create(Car car);
    public Car findById(String id);
    public void delete(String id);
    public void update(Car car);
}
