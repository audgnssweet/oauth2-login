package com.study.security1.controller;

import com.study.security1.auth.PrincipalDetails;
import com.study.security1.dto.user.UserJoinDto;
import com.study.security1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final UserService userService;

    /*
    일반 유저 관련. social login 아님.
    User정보 바인딩하는 2가지 방법
    social과 binding하는 변수타입이 다름에 유의해야한다. -> 다형성 구현해줘야함.
     */
    @ResponseBody
    @GetMapping("/test/userinfo")
    public String test(
        Authentication authentication,  //Authentication은 Spring쪽에서 자동 binding
        @AuthenticationPrincipal PrincipalDetails two   //어노테이션 + type 통해서 바인딩도 가능.
    ) {
        final PrincipalDetails one = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("==============================일반유저");
        System.out.println(one.getUser());
        System.out.println(two.getUser());
        return "테스트 완료";
    }

    /*
    social 유저 관련. 일반 유저 아님.
    OAuth2 User정보 바인딩하는 2가지 방법
    */
    @ResponseBody
    @GetMapping("/test/oauth/userinfo")
    public String socialtest(
        Authentication authentication,  //Authentication은 Spring쪽에서 자동 binding
        @AuthenticationPrincipal OAuth2User oAuth2User  //마찬가지로 어노테이션 + type보고 자동바인딩.
    ) {
        final OAuth2User user = (OAuth2User) authentication.getPrincipal();
        System.out.println("==============================소셜유저");
        System.out.println(user.getAttributes());
        System.out.println(oAuth2User.getAttributes());
        //oAuth2User은 Oauth2UserService에서 super.loadUser(request)로 받은 것과 같음.
        return "테스트 완료";
    }

    @ResponseBody
    @GetMapping("/test/oauth/all/userinfo")
    public String alltest(
        Authentication authentication,
        @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        System.out.println(((PrincipalDetails) authentication.getPrincipal()).getUser());
        System.out.println(principalDetails.getUser());
        return "테스트 완료";
    }

    @GetMapping("/")
    public String index() {
        //mustache 기본 폴더 - src/main/resources
        //뷰 리졸버 설정을 원래 해줘야하는데,
        //mustache는 spring진영에서 사용을 권장하는 template engine이기 때문에
        //springboot-starter-mustache 의존성을 등록해주면, 따로 뷰 리졸버를 설정하지 않아도 된다.
        return "index"; //원래 .mustache로 찾는다.
        //기본적으로 시큐리티를 얹으면 모든 페이지가 인증을 필요로 하게되고,
        // /login, /logout, user password가 설정된다.
    }

    @ResponseBody
    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @ResponseBody
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @ResponseBody
    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    //login이라는 주소는 spring security가 낚아채버린다.
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginform";
    }

    @PostMapping("/join")
    public String join(UserJoinDto userJoinDto) {
        userService.join(userJoinDto);
        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinform";
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})  //권한 관련 설정 가능.
    @ResponseBody
    @GetMapping("/info")
    public String privateInfo() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")   //마찬가지.
    @ResponseBody
    @GetMapping("/data")
    public String privateData() {
        return "개인정보";
    }
}
