package com.study.security1.auth.oauth.provider;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

@NoArgsConstructor
@AllArgsConstructor
public class FacebookUserInfo implements OAuth2UserInfo {

    private OAuth2UserRequest userRequest;
    private OAuth2User oAuth2User;

    @Override
    public String getUsername() {
        return getProvider() + "_" + getProviderId();
    }

    @Override
    public String getEmail() {
        return (String) oAuth2User.getAttributes().get("email");
    }

    @Override
    public String getProvider() {
        return userRequest.getClientRegistration().getClientName();
    }

    @Override
    public String getProviderId() {
        return oAuth2User.getName();
    }

    @Override
    public void setRequest(OAuth2UserRequest request) {
        this.userRequest = request;
    }

    @Override
    public void setUser(OAuth2User user) {
        this.oAuth2User = user;
    }

}
