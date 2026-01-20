package com.expanse_tracker.controller;

import com.expanse_tracker.controller.dto.TopCategoryDTO;
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
import java.util.List;

@RestController
@RequestMapping("/analytics")
public class ExpenseAnalyticsController {

    private final ExpenseAnalyticsService expenseAnalyticsService;

    public ExpenseAnalyticsController(ExpenseAnalyticsService expenseAnalyticsService) {
        this.expenseAnalyticsService = expenseAnalyticsService;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> summary( @RequestParam(required = false) DateRangeType filter,
                                      @RequestParam(required = false) String category,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                      Authentication authentication){
        Double suma = expenseAnalyticsService.summary(authentication.getName(), filter, category,from,to);
        return ResponseEntity.ok(suma);
    }

    @GetMapping("/summary/top-categories")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TopCategoryDTO>> topCategories(@RequestParam(name = "filter", defaultValue = "THIS_WEEK") DateRangeType type, Authentication authentication) {

        return ResponseEntity.ok(expenseAnalyticsService.topCategories(authentication.getName(),type));
    }

    @GetMapping("/summary/monthly-comparison")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> variation(Authentication authentication) {

        return ResponseEntity.ok(expenseAnalyticsService.getVaration(authentication.getName()));

    }

}
