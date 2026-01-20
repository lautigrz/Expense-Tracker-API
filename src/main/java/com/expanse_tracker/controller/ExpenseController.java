package com.expanse_tracker.controller;

import com.expanse_tracker.controller.dto.ExpenseRequestDTO;
import com.expanse_tracker.enums.DateRangeType;
import com.expanse_tracker.service.ExpenseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDate;

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

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getExpenses(
            @RequestParam(required = false) DateRangeType filter,
            @RequestParam(required = false) String category,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                expenseService.getExpenses(
                        authentication.getName(),
                        filter,
                        category,
                        from,
                        to
                )
        );
    }

}
