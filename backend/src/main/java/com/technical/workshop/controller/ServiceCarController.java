package com.technical.workshop.controller;

import com.technical.workshop.model.Car;
import com.technical.workshop.model.DTO.CarDTO;
import com.technical.workshop.model.DTO.CarOwnerDTO;
import com.technical.workshop.model.ServiceCar;
import com.technical.workshop.model.User;
import com.technical.workshop.repositories.CarRepository;
import com.technical.workshop.repositories.UserRepository;
import com.technical.workshop.service.CarService;
import com.technical.workshop.service.ServiceCarService;
import com.technical.workshop.service.UserService;
import com.technical.workshop.service.impl.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/car/{id}/service")
public class ServiceCarController {
    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;
    @Autowired
    private ServiceCarService serviceCarService;

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    private JwtServiceImpl jwtServiceImpl;

//    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity<ServiceCar> findById(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            Car car = serviceCarService.findById(id);
//            if (car == null) {
//                throw new RuntimeException("Erro");
//            }
//            return ResponseEntity.ok().body(new CarDTO((car)));
//        }
//        throw new RuntimeException("Erros");
//    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody ServiceCar serviceCar, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(serviceCar.getId()).toUri();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            var email = jwtServiceImpl.tokenValidator(token);
            User user = userRepository.findByEmailLike(email);
            if (user.getCar() != null) {
                serviceCar.setCar(user.getCar());
                serviceCarService.create(serviceCar);
                Optional<Car> car = carRepository.findById(user.getCar().getId());
                if(car.isPresent()){
                    car.get().setServiceCar(serviceCar);
                    carRepository.save(car.get());
                }
                return ResponseEntity.created(uri).build();
            }
            throw new RuntimeException("User doesn't have a registered car");
        }
        return ResponseEntity.created(uri).build();
    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<Void> delete(@PathVariable String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.substring(7);
//            var email = jwtServiceImpl.tokenValidator(token);
//            User user = userRepository.findByEmailLike(email);
//            if (user.getCar() != null) {
//                if (Objects.equals(user.getCar().getId(), id)) {
//                    carService.delete(id);
//                    user.setCar(null);
//                    userRepository.save(user);
//                    return ResponseEntity.noContent().build();
//                }
//                throw new RuntimeException("Car not found");
//            }
//            throw new RuntimeException("User doens't have a registered car");
//        }
//        return ResponseEntity.noContent().build();
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
//    public ResponseEntity<Void> update(@RequestBody Car car, @PathVariable String id) {
//        car.setYear(car.getYear());
//        car.setBrand(car.getBrand());
//        car.setLicensePlate(car.getLicensePlate());
//        car.setId(id);
//        carService.update(car);
//        return ResponseEntity.noContent().build();
//    }

}
