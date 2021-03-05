package com.study.security1.auth;

import com.study.security1.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

/*
로그인이 완료되면 security session을 만들어준다.
같은 session공간이지만 security가 관리. -> 키 값으로 구분
securityContextHolder라는 키값에.
security가 관리하는 session에 들어갈 수 있는 객체는 Authentication 객체.
Authentication 안에 유저 정보가 있어야 하는데, 이 정보를 갖고있는 객체도 정해져있음
UserDetails라는 객체.

정리
Security session (contextHolder) -> Authentication -> UserDetails -> User정보
 */
@Getter
@AllArgsConstructor
public class PrincipalDetails implements UserDetails, OAuth2User {

    private final User user;  //콤포지션 해준다.

    //OAuth2User의 요구사항을 맞추기 위함.
    /*
    그러니까 로그인을 할 때 OAuth2User로 하면, Authentication에 PrincipalDetails로 변환해서 넘겨 줄 때
    일반 유저처럼 그냥 user바로 넣어주는게 아니고 attributes까지 넣어줘야하기 때문에
    만들어준다.
     */
    private Map<String, Object> attributes;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    //해당 유저의 권한을 return. return type을 맞춰줘야한다.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> {
            return "ROLE_" + user.getRole()
                .toString();   //필요함수가 한개고, 매개변수 없어서 return만 해주면 스트림으로들어감.
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정 만료됐니? true - 아니오 false - 네
    @Override
    public boolean isAccountNonExpired() {
        /*
        예시로 Entity table에 가장 최근 로그인 날짜를 기록하게 하고,
        그 시간을 가져와서 현재 시간과 비교, 일정 기간 이상이면 false를 반환하게 한다던가
        company dependent한 로직이 들어간다.
         */
        return true;
    }

    //계정 잠겼니? 위와같음.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //계정 비밀번호 만료됐니?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 비활성화 되어있니?
    @Override
    public boolean isEnabled() {
        return true;
    }

    /*
    OAuth2와의 다형성을 위한 구현
     */
    @Override
    public String getName() {
        return (String) attributes.get("sub");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

}
