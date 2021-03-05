package com.study.security1.service;

import com.study.security1.dto.user.UserInfoResponseDto;
import com.study.security1.dto.user.UserJoinDto;

public interface UserService {

    UserInfoResponseDto join(UserJoinDto userJoinDto);
}
