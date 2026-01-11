package com.expanse_tracker.controller.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponseDTO {

    private Long id;

    private String description;

    private Double amount;

    private CategoryDTO category;

    private LocalDate date;

}
