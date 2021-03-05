package com.study.security1.config;

import com.study.security1.auth.PrincipalDetailsService;
import com.study.security1.auth.oauth.Oauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity  //시큐리티 필터가 필터체인에 등록됨.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)  //@Secured 활성화. @PreAuthorize 활성화.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalDetailsService principalDetailsService;

    private final Oauth2UserService oauth2UserService;

    @Bean   //리턴되는 오브젝트를 IOC 컨테이너에 등록해준다.
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()   //csrf 비활성화
            .authorizeRequests()
            .antMatchers("/user/**").authenticated()    //user이하 경로는 인증(로그인)이 필요해
            .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
            //위. manager 이하는 ADMIN 권한이나 MANAGER 권한이 필요해.
            .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
            .anyRequest().permitAll()  //이외 다른 모든 요청은 누구에게나 허용
            //아래 설정이 있어야 권한이 요구되는 사항이 있을 때, 로그인 페이지로 가진다.
            .and()
            .formLogin()
            .loginPage("/loginForm")
            .loginProcessingUrl("/login")   //해당 주소로 로그인 요청하면 security에서 낚아채서 로그인 해줌.
            .defaultSuccessUrl("/")
            //oauth2 관련 설정.
            .and()
            .oauth2Login()
            .loginPage("/loginForm")
            //여기까지만해도 '인증'은 가능. 즉 로그인이 된다. 그리고 인증이 필요한 페이지 들어가짐.
            //인증이 되었다는 것 - 코드를 받았다는 것.
            //1. 코드 받기 (인증) 2. 엑세스 토큰(권한) 3. 사용자 프로필 정보를 가져옴
            //4 - 1 정보가 충분 하다면 그대로 회원가입을 진행시킴
            //4 - 2 정보가 불충분한 경우 추가 회원가입 창이 떠서 정보를 더 받아서 가입시킴.
            //원래는 코드를 받아서 토큰 요청하고 토큰으로 정보 받아오고 해야하는데
            //oauth2 쓰면 oauth2가 로그인 인증처리가 됨과 동시에 (토큰 + 프로필 정보) 가져다줌.
            .userInfoEndpoint()
            .userService(oauth2UserService); //Oauth2 UserService Type의 객체 넣어줘야함.
    }
}
