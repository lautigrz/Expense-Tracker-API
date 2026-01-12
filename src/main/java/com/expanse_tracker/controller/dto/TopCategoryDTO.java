package com.expanse_tracker.controller.dto;

import com.expanse_tracker.models.ECategory;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopCategoryDTO {

    private ECategory category;
    private String colorCategory;
    private Double categoryPrice;
}
