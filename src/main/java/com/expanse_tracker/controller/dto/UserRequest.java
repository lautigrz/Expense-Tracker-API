package com.expanse_tracker.controller.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    public String username;
    public String password;
    public Set<String> roles;

}
