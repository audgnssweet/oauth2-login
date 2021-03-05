package com.study.security1.auth.oauth.provider;

public class GoogleUserInfo extends AbstractOAuth2UserInfo {

    @Override
    public String getEmail() {
        return (String) oAuth2User.getAttributes().get("email"); //Google
    }

    @Override
    public String getProviderId() {
        return oAuth2User.getName();
    }

}
