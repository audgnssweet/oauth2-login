package com.study.security1.entity;

import com.study.security1.domain.Role;
import com.study.security1.dto.user.UserJoinDto;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor  //Jpa Entity가 되기 위한 필수조건.
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;

    private String providerId;

    @CreationTimestamp
    private LocalDateTime createDate;

    public User(UserJoinDto userJoinDto) {
        this.username = userJoinDto.getUsername();
        this.password = userJoinDto.getPassword();
        this.email = userJoinDto.getEmail();
        this.role = Role.USER;
    }

    public User(UserJoinDto userJoinDto, String provider, String providerId) {
        this(userJoinDto);
        this.provider = provider;
        this.providerId = providerId;
    }

}
