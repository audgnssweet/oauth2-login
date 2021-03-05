package com.study.security1.auth.oauth.provider;

import java.util.Map;

public class NaverUserInfo extends AbstractOAuth2UserInfo {

    @Override
    public String getEmail() {
        Map map = (Map) oAuth2User.getAttributes().get("response");
        return (String) map.get("email");
    }

    @Override
    public String getProviderId() {
        Map map = (Map) oAuth2User.getAttributes().get("response");
        return (String) map.get("id");
    }

}
