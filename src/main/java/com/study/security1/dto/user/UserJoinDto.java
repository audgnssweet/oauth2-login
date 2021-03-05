package com.study.security1.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserJoinDto {

    private String username;
    private String password;
    private String email;

    @Override
    public String toString() {
        return "UserJoinDto{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            '}';
    }
}
