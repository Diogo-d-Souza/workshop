package com.technical.workshop.controller;

import com.technical.workshop.model.CarMaintenance;
import com.technical.workshop.model.User;
import com.technical.workshop.repositories.UserRepository;
import com.technical.workshop.service.CarMaintenanceService;
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
@Tag(name = "Car Maintenance")
@RequestMapping(value = "/car/{id}/maintenance")
public class CarMaintenanceController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JwtServiceImpl jwtServiceImpl;
    @Autowired
    CarMaintenanceService carMaintenanceService;
    @Operation(summary = "Delete a car maintenance. Must be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car maintenance deleted successfully"),
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<CarMaintenance> findById(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            CarMaintenance carMaintenance = carMaintenanceService.findById(id);
            if (carMaintenance == null) {
                throw new RuntimeException("Maintenance doesn't exist");
            }
            return ResponseEntity.ok().body(carMaintenance);
        }
        throw new RuntimeException("No token authorization");
    }
    @Operation(summary = "Create a car maintenance. Must not be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car maintenance created successfully"),
    })
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
                carMaintenanceService.create(carMaintenance);
                userRepository.save(user);
                return ResponseEntity.created(uri).build();
            }
            throw new RuntimeException("User doesn't have a registered car");
        }
        return ResponseEntity.created(uri).build();
    }
    @Operation(summary = "Delete a car maintenance. Must be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car maintenance deleted successfully"),
    })
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            var email = jwtServiceImpl.tokenValidator(token);
            User user = userRepository.findByEmailLike(email);
            if (user.getCar() != null) {
                if (Objects.equals(user.getId(), id)) {
                    carMaintenanceService.delete(id);
                    user.setCarMaintenance(null);
                    userRepository.save(user);
                    return ResponseEntity.noContent().build();
                }
                throw new RuntimeException("User not found");
            }
            throw new RuntimeException("User doesn't have a registered car");
        }
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Update a car maintenance. Must be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car maintenance edited successfully"),
    })
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
                    carMaintenanceService.update(carMaintenance);
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
