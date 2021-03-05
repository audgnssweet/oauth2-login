package com.study.security1.auth.oauth;

import com.study.security1.auth.PrincipalDetails;
import com.study.security1.auth.oauth.provider.OAuth2UserInfo;
import com.study.security1.dto.user.UserJoinDto;
import com.study.security1.entity.User;
import com.study.security1.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final List<String> providers = Arrays.asList("Google", "Facebook", "Naver");

    /*
    로그인 후처리함수. - google로부터 받은
    userRequest를 후처리하는 함수. (엑세스 토큰 + 프로필)
    이 함수도 마찬가지로 PrincipalDetails를 return하여 일반 유저와의 다형성을 위해 오버라이딩.
    그냥 OAuth2User return 하려면 오버라이딩 안해도 돼. 단지
    다형성이 없어서 유지보수하기가 매우 어려워질 뿐.

    해당 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.

    여기서 반환되는 값이 Authentication 안으로 들어가기 때문에
    PrincipalDetails로 반환해줘야 한다.
    또한 첫 로그인이라면 자동가입을 해주고
    첫 로그인이 아니라면 바로 PrincipalDetails를 return 해주면 된다.
     */
    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        /*
        super.loadUser(userRequest).getAttributes(); 안에 Map 형식으로 프로필 데이터들이 들어있음.
        userRequest -> OAuth2에서 코드 받고, 엑세스토큰 요청해서 그거까지 받아서 넣어준 것.
        super.loadUser을 호출하면? 엑세스토큰으로 프로필 정보를 가져온다.
         */

        /*
        얘는 미리 회원가입을 할 수 있는게 아니라 구글측에서 요청을 가로채기 때문에, 내가 control 할 수 있는 부분이 여기.
        여기서 회원인지 아닌지 아니라면 가입처리 등을 해줘야한다.
         */
        String requestProvider = userRequest.getClientRegistration().getClientName();
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        final Optional<String> foundProvider = providers.stream()
            .filter(provider -> provider.equals(requestProvider))
            .findFirst();

        final String provider = foundProvider.orElseThrow(IllegalAccessError::new);

        OAuth2UserInfo oAuth2UserInfo = getoAuth2UserInfo(userRequest, oAuth2User, provider);

        String username = oAuth2UserInfo.getUsername();
        String email = oAuth2UserInfo.getEmail();
        String providerId = oAuth2UserInfo.getProviderId();

        final Optional<User> foundUser = userRepository.findUserByUsername(username);
        //회원가입
        User user = foundUser.orElseGet(() -> joinSocial(username, email, provider, providerId));

        //여기 return값을 authentication 객체 안에 넣어준다.
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

    private OAuth2UserInfo getoAuth2UserInfo(OAuth2UserRequest userRequest, OAuth2User oAuth2User,
        String provider) {
        //동적 객체 생성.
        String path = "com.study.security1.auth.oauth.provider.";
        try {
            //경로까지 지정해야함.
            OAuth2UserInfo oAuth2UserInfo =
                (OAuth2UserInfo) Class.forName(path + provider + "UserInfo").newInstance();
            oAuth2UserInfo.setRequest(userRequest);
            oAuth2UserInfo.setUser(oAuth2User);
            return oAuth2UserInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //강제회원가입
    private User joinSocial(String username, String email, String provider, String providerId) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();    //무한참조 방지
        String socialPassword = passwordEncoder.encode(provider);
        UserJoinDto userJoinDto = new UserJoinDto(username, socialPassword, email);
        User user = new User(userJoinDto, provider, providerId);
        return userRepository.save(user);
    }

}
