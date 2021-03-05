package com.study.security1.auth.oauth.provider;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractOAuth2UserInfo implements OAuth2UserInfo {

    protected OAuth2UserRequest userRequest;
    protected OAuth2User oAuth2User;

    @Override
    public String getUsername() {
        return getProvider() + "_" + getProviderId();
    }

    @Override
    public String getProvider() {
        return userRequest.getClientRegistration().getClientName();
    }

    @Override
    public void setRequest(OAuth2UserRequest request) {
        this.userRequest = request;
    }

    @Override
    public void setUser(OAuth2User user) {
        this.oAuth2User = user;
    }

    @Override
    public abstract String getEmail();

    @Override
    public abstract String getProviderId();

}
