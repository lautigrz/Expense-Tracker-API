package com.expanse_tracker.controller.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    public String username;
    public String password;
    public String email;
    public Set<String> roles;

}
