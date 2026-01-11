package com.expanse_tracker.controller;

import com.expanse_tracker.enums.DateRangeType;
import com.expanse_tracker.service.ExpenseAnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/analytics")
public class ExpenseAnalyticsController {

    private final ExpenseAnalyticsService expenseAnalyticsService;

    public ExpenseAnalyticsController(ExpenseAnalyticsService expenseAnalyticsService) {
        this.expenseAnalyticsService = expenseAnalyticsService;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> summary(@RequestParam(name = "filter", defaultValue = "THIS_WEEK") DateRangeType type, Authentication authentication){
        Double suma = expenseAnalyticsService.summary(authentication.getName(), type);
        return ResponseEntity.ok(suma);
    }


    @GetMapping("/top-categories")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> topCategories(@RequestParam(name = "filter", defaultValue = "THIS_WEEK") DateRangeType type, Authentication authentication) {

        return ResponseEntity.ok(expenseAnalyticsService.topCategories(authentication.getName(),type));
    }



    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> filterExpenses(@RequestParam(name = "filter", defaultValue = "THIS_WEEK") DateRangeType type, Authentication authentication) {

        return ResponseEntity.ok(expenseAnalyticsService.filter(authentication.getName(),type));

    }


    @GetMapping("/filter-category")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> filterCategory(@RequestParam("category") String category, Authentication authentication) {

        return ResponseEntity.ok(expenseAnalyticsService.filterByCategory(authentication.getName(),category));

    }

    @GetMapping("/filter-custom")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> filterCustom(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin, Authentication authentication) {

        return ResponseEntity.ok(expenseAnalyticsService.filterCustom(authentication.getName(),fechaInicio,fechaFin));
    }

}
