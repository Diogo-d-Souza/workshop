package com.technical.workshop.controller;

import com.technical.workshop.model.CarMaintenance;
import com.technical.workshop.model.User;
import com.technical.workshop.repositories.CarRepository;
import com.technical.workshop.repositories.UserRepository;
import com.technical.workshop.service.CarMaintanceService;
import com.technical.workshop.service.CarService;
import com.technical.workshop.service.ServiceCarService;
import com.technical.workshop.service.UserService;
import com.technical.workshop.service.impl.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping(value = "/car/{id}/maintenance")
public class CarMaintanceController {
    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;
    @Autowired
    private ServiceCarService serviceCarService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    private JwtServiceImpl jwtServiceImpl;
    @Autowired
    CarMaintanceService carMaintanceService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<CarMaintenance> findById(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            CarMaintenance carMaintenance = carMaintanceService.findById(id);
            if (carMaintenance == null) {
                throw new RuntimeException("Service doesn't exist");
            }
            return ResponseEntity.ok().body(carMaintenance);
        }
        throw new RuntimeException("No token authorization");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody CarMaintenance carMaintenance, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(carMaintenance.getId()).toUri();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            var email = jwtServiceImpl.tokenValidator(token);
            User user = userRepository.findByEmailLike(email);
            if (user.getCar() != null) {
                user.setCarMaintenance(carMaintenance);
                carMaintanceService.create(carMaintenance);
                userRepository.save(user);
                return ResponseEntity.created(uri).build();
            }
            throw new RuntimeException("User doesn't have a registered car");
        }
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            var email = jwtServiceImpl.tokenValidator(token);
            User user = userRepository.findByEmailLike(email);
            if (user.getCar() != null) {
                if (Objects.equals(user.getId(), id)) {
                    carMaintanceService.delete(id);
                    user.setCarMaintenance(null);
                    userRepository.save(user);
                    return ResponseEntity.noContent().build();
                }
                throw new RuntimeException("User not found");
            }
            throw new RuntimeException("User doens't have a registered car");
        }
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@RequestBody CarMaintenance carMaintenance, @PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            var email = jwtServiceImpl.tokenValidator(token);
            User user = userRepository.findByEmailLike(email);
            if (user.getCar() != null) {
                if (Objects.equals(user.getId(), id)) {
                    carMaintenance.setMaintenanceName(carMaintenance.getMaintenanceName());
                    carMaintenance.setId(id);
                    carMaintanceService.update(carMaintenance);
                    user.setCarMaintenance(carMaintenance);
                    userRepository.save(user);
                    return ResponseEntity.noContent().build();
                }
                throw new RuntimeException("User not found");
            }
            throw new RuntimeException("User doens't have a registered car");
        }

        return ResponseEntity.noContent().build();
    }

}
