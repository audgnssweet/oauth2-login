package com.study.security1.auth.oauth.provider;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserInfo {

    String getUsername();

    String getEmail();

    String getProvider();

    String getProviderId();

    void setRequest(OAuth2UserRequest request);

    void setUser(OAuth2User user);
}
