package com.study.security1.auth.oauth.provider;

public class FacebookUserInfo extends AbstractOAuth2UserInfo {

    @Override
    public String getEmail() {
        return (String) oAuth2User.getAttributes().get("email");
    }

    @Override
    public String getProviderId() {
        return oAuth2User.getName();
    }

}
