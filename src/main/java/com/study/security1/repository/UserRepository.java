package com.study.security1.repository;

import com.study.security1.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String username);   //JPA의 query method 전략.
    boolean existsByUsername(String username);
}
