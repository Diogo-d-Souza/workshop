package com.technical.workshop.controller;

import com.technical.workshop.model.Car;
import com.technical.workshop.model.DTO.CarDTO;
import com.technical.workshop.model.DTO.CarOwnerDTO;
import com.technical.workshop.model.User;
import com.technical.workshop.repositories.UserRepository;
import com.technical.workshop.service.CarService;
import com.technical.workshop.service.impl.JwtServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Cars")
@RequestMapping(value = "/car")
public class CarController {
    @Autowired
    private CarService carService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JwtServiceImpl jwtServiceImpl;
    @Operation(summary = "Delete a car. Must be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car deleted successfully"),
    })
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
    @Operation(summary = "Create a car. Must not be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car created successfully"),
    })
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
    @Operation(summary = "Delete a car. Must be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car deleted successfully"),
    })
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
    @Operation(summary = "Update a car. Must be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car edited successfully"),
    })
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
