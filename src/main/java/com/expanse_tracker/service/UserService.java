package com.expanse_tracker.service;

import com.expanse_tracker.controller.dto.ExpenseResponse;
import com.expanse_tracker.controller.dto.UserRequest;
import com.expanse_tracker.controller.dto.UserWithExponsesDTO;
import com.expanse_tracker.models.ERole;
import com.expanse_tracker.models.ExpenseEntity;
import com.expanse_tracker.models.RoleEntity;
import com.expanse_tracker.models.UserEntity;
import com.expanse_tracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

   public UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserWithExponsesDTO getUserExpenses(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ExpenseResponse> expenseResponses = user.getExpenses()
                .stream()
                .map(expense -> ExpenseResponse.builder()
                        .username(user.getUsername())
                        .description(expense.getDescription())
                        .amount(expense.getAmount())
                        .category(expense.getCategory().getCategory().name())
                        .build())
                .toList();
        return UserWithExponsesDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .expenses(expenseResponses)
                .build();

    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

//    public void saveExpenseToUser(ExpenseEntity expense, Long userId) {
//
//        UserEntity user = findById(userId);
//        user.getExpenses().add(expense);
//        userRepository.save(user);
//    }

}


