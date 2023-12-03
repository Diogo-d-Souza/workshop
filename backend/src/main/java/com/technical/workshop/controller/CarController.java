package com.technical.workshop.controller;

import com.technical.workshop.model.Car;
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
    public ResponseEntity<UserDTO> findById(@PathVariable String id) {
        com.technical.workshop.model.User user = userService.findById(id);
        return ResponseEntity.ok().body(new UserDTO((user)));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody Car car, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            var email = jwtServiceImpl.tokenValidator(token);
            User user = userRepository.findByEmailLike(email);
            CarOwnerDTO carOwnerDTO = new CarOwnerDTO(user);
            car.setUser(carOwnerDTO);
            carService.create(car);
        }

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(car.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }


}
