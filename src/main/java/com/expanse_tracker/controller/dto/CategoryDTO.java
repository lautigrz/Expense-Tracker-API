package com.expanse_tracker.controller.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private String categoryName;

    private String background;

    private String color;

    private String icon;

}
