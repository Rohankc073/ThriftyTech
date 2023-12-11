

package com.thriftytech.ecommerce.Controller;

import com.thriftytech.ecommerce.Entity.User;
import com.thriftytech.ecommerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173") // Change the URL value for it to work on your server
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.createUser(user);
            String successMessage = "Registration successful";

            // Construct the response as a Map
            Map<String, Object> response = new HashMap<>();
            response.put("message", successMessage);
            response.put("user", savedUser);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            String errorMessage = "Registration failed: " + e.getMessage();
            // Construct the error response as a Map
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", errorMessage);

            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody User loginUser) {
        try {
            // Log the received values
            System.out.println("Received username: " + loginUser.getUsername());
            System.out.println("Received password: " + loginUser.getPassword());

            // Use the loginUser method from the UserService
            User user = userService.loginUser(loginUser.getUsername(), loginUser.getPassword());

            // Create a response map with user information appearing first
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("message", "Login successful");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Create an error response map
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Login failed: " + e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    //
}