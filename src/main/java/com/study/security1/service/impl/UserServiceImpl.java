package com.study.security1.service.impl;

import com.study.security1.dto.user.UserInfoResponseDto;
import com.study.security1.dto.user.UserJoinDto;
import com.study.security1.entity.User;
import com.study.security1.repository.UserRepository;
import com.study.security1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserInfoResponseDto join(UserJoinDto userJoinDto) {
        userJoinDto.setPassword(passwordEncoder.encode(userJoinDto.getPassword()));
        final User savedUser = userRepository.save(new User(userJoinDto));
        return new UserInfoResponseDto(savedUser);
    }

}
