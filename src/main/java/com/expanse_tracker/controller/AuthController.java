package com.expanse_tracker.controller;

import com.expanse_tracker.controller.dto.UserRequest;
import com.expanse_tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest user) {

        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping("/hello")
    @PreAuthorize("hasRole('USER')")
    public String hello() {
        return "Hello, User!";
    }


}
