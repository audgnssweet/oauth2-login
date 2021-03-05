package com.study.security1.auth.oauth.provider;

import java.util.Map;

public class KakaoUserInfo extends AbstractOAuth2UserInfo{

    @Override
    public String getEmail() {
        Map map = (Map) oAuth2User.getAttributes().get("kakao_account");
        return (String) map.get("email");
    }

    @Override
    public String getProviderId() {
        return (oAuth2User.getAttributes().get("id")).toString();
    }
}
