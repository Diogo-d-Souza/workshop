package com.technical.workshop.controller;

import com.technical.workshop.model.DTO.UserDTO;
import com.technical.workshop.repositories.UserRepository;
import com.technical.workshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
@Tag(name = "User")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Operation(summary = "Get all users. Must be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all users successfully"),
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> findAll() {
        List<com.technical.workshop.model.User> list = userService.listAll();
        List<UserDTO> listDTO = list.stream().map(UserDTO::new).toList();
        return ResponseEntity.ok().body(listDTO);
    }
    @Operation(summary = "Get one user. Must be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get users successfully"),
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> findById(@PathVariable String id) {
        com.technical.workshop.model.User user = userService.findById(id);
        return ResponseEntity.ok().body(new UserDTO((user)));
    }
    @Operation(summary = "Create a user. Must not be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "500", description = "User already exists"),
    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody com.technical.workshop.model.User user) {
        var userExists = userRepository.findByEmailLike(user.getEmail());
        if(userExists != null){
            throw new RuntimeException("User already Exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user = userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
    @Operation(summary = "Delete a user. Must be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(summary = "Update a user. Must be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User edited successfully"),
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@RequestBody com.technical.workshop.model.User user, @PathVariable String id) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        userService.update(user);
        return ResponseEntity.noContent().build();
    }

}
