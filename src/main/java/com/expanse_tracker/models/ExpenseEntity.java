package com.expanse_tracker.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String description;

    public Double amount;

    @OneToOne(cascade = CascadeType.ALL)
    public CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public UserEntity user;
}
