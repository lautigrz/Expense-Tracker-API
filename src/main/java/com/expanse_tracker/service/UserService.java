package com.expanse_tracker.service;

import com.expanse_tracker.controller.dto.UserDTO;
import com.expanse_tracker.exception.UserNotFound;
import com.expanse_tracker.mapper.Mapper;
import com.expanse_tracker.models.UserEntity;
import com.expanse_tracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public UserDTO saveUser(UserDTO userRequest) {

        userRepository.findByEmail(userRequest.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Email existente");
                });

        UserEntity userEntity = Mapper.toEntity(userRequest);
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        return Mapper.toDTO(userRepository.save(userEntity));
    }


    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFound("User not found"));
    }

}


