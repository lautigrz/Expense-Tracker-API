package com.expanse_tracker.controller.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWithExponsesDTO {

    private Long id;
    private String username;
    private List<ExpenseResponse> expenses;

}
