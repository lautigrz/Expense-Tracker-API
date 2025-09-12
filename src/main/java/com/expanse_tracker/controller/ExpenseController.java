package com.expanse_tracker.controller;

import com.expanse_tracker.controller.dto.ExpenseRequest;
import com.expanse_tracker.service.ExpenseService;
import com.expanse_tracker.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;
    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }


    @PostMapping("/expenses")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addExpense(@RequestBody ExpenseRequest expenseRequest) {
        return ResponseEntity.ok(expenseService.addExpense(expenseRequest));
    }

    @DeleteMapping("/expensesDelete/{expenseId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteExpense(@PathVariable Long expenseId, Authentication authentication) {
        expenseService.deleteUserExpense(authentication.getName(), expenseId);
        return ResponseEntity.ok("Expense deleted successfully!");
    }

    @PutMapping("/expensesUpdate/{expenseId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateExpense(@PathVariable Long expenseId, @RequestBody ExpenseRequest expenseRequest, Authentication authentication) {
        try {
            return ResponseEntity.ok(expenseService.updateUserExpense(authentication.getName(), expenseId, expenseRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/filterExpenses")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> filterExpenses(@RequestParam("filter") Integer filter, Authentication authentication) {

        return ResponseEntity.ok(expenseService.filter(authentication.getName(),filter));

    }


    @GetMapping("/filterCustom")
    public ResponseEntity<?> filterCustom(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin, Authentication authentication) {

        return ResponseEntity.ok(expenseService.filterCustom(authentication.getName(),fechaInicio,fechaFin));
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> listExpenses(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserExpenses(authentication.getName()));
    }

}
