package com.study.security1.auth;

import com.study.security1.entity.User;
import com.study.security1.repository.UserRepository;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
시큐리티 설정에서 loginProcessingUrl("/login") 설정 때문에
/login 요청이 들어오면 시큐리티에서 요청을 낚아채서
UserDetailsService 타입의 객체를 IOC 컨테이너에서 띄워서
loadUserByUsername 함수를 실행시킨다.
함수 내에서 파라미터로 넘겨받은 username으로 UserDetails를 만들어서 넘겨주면 된다.
없으면 null을 return해주면 로그인이 안된다.
*/

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /*
    해당 함수를 수행하고나면
    Security session ( Authentication ( UserDetails - PrincipalDetails) ) ) 와 같은 형태가 된다.
    오버라이딩을 굳이 해주는 이유는 PrincipalDetails 객체를 return하기위해
    -> social과의 다형성을위해
    그냥 UserDetails를 return 하려면 override 안해도 돼.

    해당 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> foundUser = userRepository.findUserByUsername(username);
        //여기 return 값을 Authentication 객체 안에 넣어준다.
        //얘는 일반 로그인이니까 PrincipalDetails안에 User만 넣어주면 돼.
        return foundUser.map(PrincipalDetails::new).orElse(null);
    }
}
