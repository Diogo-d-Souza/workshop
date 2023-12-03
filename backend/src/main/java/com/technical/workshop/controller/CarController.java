package com.technical.workshop.controller;

import com.technical.workshop.model.Car;
import com.technical.workshop.model.DTO.CarDTO;
import com.technical.workshop.model.DTO.CarOwnerDTO;
import com.technical.workshop.model.DTO.UserDTO;
import com.technical.workshop.model.User;
import com.technical.workshop.repositories.UserRepository;
import com.technical.workshop.service.CarService;
import com.technical.workshop.service.UserService;
import com.technical.workshop.service.impl.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/car")
public class CarController {
    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JwtServiceImpl jwtServiceImpl;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CarDTO> findById(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            Car car = carService.findById(id);
            if (car == null) {
                throw new RuntimeException("Car doesn't exist");
            }
            return ResponseEntity.ok().body(new CarDTO((car)));
        }
        throw new RuntimeException("No token authorization");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody Car car, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(car.getId()).toUri();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            var email = jwtServiceImpl.tokenValidator(token);
            User user = userRepository.findByEmailLike(email);
            if (user.getCar() == null) {
                CarOwnerDTO carOwnerDTO = new CarOwnerDTO(user);
                car.setUser(carOwnerDTO);
                user.setCar(car);
                carService.create(car);
                userRepository.save(user);
                return ResponseEntity.created(uri).build();
            }
            throw new RuntimeException("User already have a registered car");
        }
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            var email = jwtServiceImpl.tokenValidator(token);
            User user = userRepository.findByEmailLike(email);
            if (user.getCar() != null) {
                if (Objects.equals(user.getCar().getId(), id)) {
                    carService.delete(id);
                    user.setCar(null);
                    userRepository.save(user);
                    return ResponseEntity.noContent().build();
                }
                throw new RuntimeException("Car not found");
            }
            throw new RuntimeException("User doesn't have a registered car");
        }
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@RequestBody com.technical.workshop.model.Car car, @PathVariable String id) {
        car.setYear(car.getYear());
        car.setBrand(car.getBrand());
        car.setLicensePlate(car.getLicensePlate());
        car.setId(id);
        carService.update(car);
        return ResponseEntity.noContent().build();
    }

}
