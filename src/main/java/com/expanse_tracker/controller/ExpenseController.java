package com.expanse_tracker.controller;

import com.expanse_tracker.controller.dto.ExpenseRequestDTO;
import com.expanse_tracker.controller.dto.ExpenseResponseDTO;
import com.expanse_tracker.service.ExpenseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create (@RequestBody ExpenseRequestDTO expenseRequest, Authentication authentication) {

        return ResponseEntity.created(URI.create("/expense/" + expenseRequest.getAmount()))
                .body(expenseService.addExpense(authentication.getName(), expenseRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {
        expenseService.deleteUserExpense(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ExpenseRequestDTO expenseRequest, Authentication authentication) {

    return ResponseEntity.ok(expenseService.updateUserExpense(authentication.getName(), id, expenseRequest));

    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ExpenseResponseDTO>> list(Authentication authentication) {
        List<ExpenseResponseDTO> expenses = expenseService.getAllExpenses(authentication.getName());
        return ResponseEntity.ok(expenses);
    }

}
