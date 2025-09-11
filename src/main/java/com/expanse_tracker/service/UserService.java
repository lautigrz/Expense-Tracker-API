package com.expanse_tracker.service;

import com.expanse_tracker.controller.dto.UserRequest;
import com.expanse_tracker.models.ERole;
import com.expanse_tracker.models.RoleEntity;
import com.expanse_tracker.models.UserEntity;
import com.expanse_tracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {


    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void saveUser(UserRequest userRequest) {

        Set<RoleEntity> roleEntities =
                userRequest.getRoles()
                        .stream()
                        .map(roleName -> RoleEntity.builder()
                                .name(ERole.valueOf(roleName))
                                .build())
                        .collect(Collectors.toSet());


        UserEntity userEntity = UserEntity.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .roles(roleEntities)
                .build();

        userRepository.save(userEntity);

    }

}


