package com.technical.workshop.controller;

import com.technical.workshop.model.DTO.LoginDTO;
import com.technical.workshop.model.DTO.LoginResponseDTO;
import com.technical.workshop.model.User;
import com.technical.workshop.service.impl.JwtServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "Login")
@RequestMapping(value = "/")
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtServiceImpl jwtServiceImpl;
    @Operation(summary = "User login. Must not be logged", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged successfully"),
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO login){
        var usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(login.email(), login.password());
        var authenticate = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        var user = (User) authenticate.getPrincipal();
        var token = jwtServiceImpl.createToken(user);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
