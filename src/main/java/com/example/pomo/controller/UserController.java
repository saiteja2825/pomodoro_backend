package com.example.pomo.controller;

import com.example.pomo.DTOs.LoginRequest;
import com.example.pomo.DTOs.LoginResponse;
import com.example.pomo.model.User;
import com.example.pomo.JWT.JwtUtil;
import com.example.pomo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    // ------------------- REGISTER -------------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        try {
            User registered = service.register(user);  // saves user to DB
            // password already set to null in service
            return ResponseEntity.ok(registered);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ------------------- LOGIN -------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User loginUser = service.login(loginRequest.getUsername(), loginRequest.getPassword());

            // generate JWT token
            String token = jwtUtil.generateToken(loginRequest.getUsername());

            // return user info + token
            return ResponseEntity.ok(new LoginResponse(loginUser, token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
