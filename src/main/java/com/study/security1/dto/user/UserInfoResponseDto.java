package com.study.security1.dto.user;

import com.study.security1.entity.User;
import lombok.Data;

@Data
public class UserInfoResponseDto {

    private Integer id;
    private String username;
    private String email;
    private String provider;

    public UserInfoResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.provider = user.getProvider();
    }
}
