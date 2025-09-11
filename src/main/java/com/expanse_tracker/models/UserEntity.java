package com.expanse_tracker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank
    public String username;

    public String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, targetEntity = RoleEntity.class)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    public Set<RoleEntity> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<ExpenseEntity> expenses;
}
