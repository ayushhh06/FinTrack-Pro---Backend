package com.tracker.expense_api.controller;

import com.tracker.expense_api.config.JwtUtils;
import com.tracker.expense_api.model.User;
import com.tracker.expense_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Real Registration API
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> signupRequest) {
        String username = signupRequest.get("username");
        String password = signupRequest.get("password");
        String name = signupRequest.get("name");

        // Loophole safeguard: Prevent duplicate account creation
        if (userRepository.findByUsername(username).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "An account with this email already exists!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        // Initialize user entity and encode plain password to secure hash
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password)); // Hashing happens here

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered safely inside database context.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid username or security password credentials.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        User user = userOptional.get();

        // GENERATE TRUE SECURITY CRYPTO SIGNED JWT STRING
        String realJwtToken = jwtUtils.generateToken(user.getUsername(), user.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("token", realJwtToken); // Returns true JWT token back to React app
        response.put("username", user.getUsername());
        response.put("name", user.getName());
        response.put("id", user.getId());

        return ResponseEntity.ok(response);
    }
}


