package com.expanse_tracker.controller.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRequestDTO {
    private String description;

    private Double amount;

    private String category;

    private LocalDate date;
}
