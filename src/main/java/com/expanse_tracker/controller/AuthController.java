package com.expanse_tracker.controller;

import com.expanse_tracker.controller.dto.UserDTO;
import com.expanse_tracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO user) {
        UserDTO userCreate = userService.saveUser(user);

        return ResponseEntity.created(URI.create("/users/" + userCreate.getUsername())).body(userCreate);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> me(Authentication authentication) {
        return ResponseEntity.ok(
                Map.of(
                        "valid", true,
                        "username", authentication.getName()
                )
        );
    }

}
