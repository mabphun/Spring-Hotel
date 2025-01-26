package com.mabphun.HotelServer.controller.auth;

import com.mabphun.HotelServer.dto.AuthenticationResponse;
import com.mabphun.HotelServer.dto.LoginRequest;
import com.mabphun.HotelServer.dto.SingupRequest;
import com.mabphun.HotelServer.dto.UserDto;
import com.mabphun.HotelServer.services.auth.AuthService;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request){
        try{
            AuthenticationResponse response = authService.loginUser(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SingupRequest singupRequest) {
        try{
            UserDto createdUser = authService.createUser(singupRequest);
            return new ResponseEntity<>(createdUser, HttpStatus.OK);
        }
        catch(EntityExistsException e){
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username){
        return new ResponseEntity<>(username, HttpStatus.OK);
    }
}
