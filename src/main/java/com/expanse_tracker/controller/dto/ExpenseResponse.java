package com.expanse_tracker.controller.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseResponse {

    private String description;
    private Double amount;
    private String category;
    private String username;

}
