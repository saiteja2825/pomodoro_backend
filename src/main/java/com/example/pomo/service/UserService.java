package com.example.pomo.service;

import com.example.pomo.model.User;
import com.example.pomo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepo repo;

    @Autowired
    public UserService(UserRepo repo){
        this.repo = repo;
    }

    // Register user
    public User register(User user){
        // Check if username exists
        if(repo.findByUsername(user.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists");
        }

        // Check if email exists
        if(user.getEmail() != null && repo.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        // Save user to DB
        User savedUser = repo.save(user);

        // Remove password before returning
        savedUser.setPassword(null);

        return savedUser;  // Return the actual saved user
    }

    // Login user
    public User login(String username, String password){
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.getPassword().equals(password)){
            throw new RuntimeException("Invalid password");
        }

        // Remove password before returning
        user.setPassword(null);

        return user;
    }

    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }

}
