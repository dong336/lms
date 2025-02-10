package com.system.lms.fo.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/* 
 * 상수 코드 값으로 사용 할 목적으로
 * read only 로 사용
 */
@Component
public class CommonVO {

    @Value("${jwt.base.key}")
    public String jwtKey;

    @Value("${google.clientId}")
    public String googleClientId;

    @Value("${google.clientSecret}")
    public String googleClientSecret;

    @Value("${google.oauth2.redirect.uri}")
    public String googleRedirectUri;

    @Value("${google.email.sender}")
    public String googleEmailSender;

    @Value("${google.app.password}")
    public String googleAppPassword;

    @Value("${google.oauth2.request.uri}")
    public String googleOauth2RequestUri;

    @Value("${google.read.scope.profile.uri}")
    public String googleReadScopeProfileUri;

    @Value("${google.read.scope.email.uri}")
    public String googleReadScopeEmailUri;

    @Value("${google.oauth2.token.uri}")
    public String googleOauth2TokenUri;

    @Value("${google.oauth2.userinfo.uri}")
    public String googleOauth2UserinfoUri;

    @Value("${google.oauth2.revoke.uri}")
    public String googleOauth2RevokeUri;
    
    @Value("${kakao.oauth2.redirect.uri}")
    public String kakaoRedirectUri;

    @Value("${kakao.clientId}")
    public String kakaoClientId;

    @Value("${kakao.oauth2.request.uri}")
    public String kakaoOauth2RequestUri;

    @Value("${kakao.oauth2.token.uri}")
    public String kakaoOauth2TokenUri;

    @Value("${kakao.oauth2.userinfo.uri}")
    public String kakaoOauth2UserinfoUri;

    @Value("${kakao.oauth2.revoke.uri}")
    public String kakaoOauth2RevokeUri;

    @Value("${naver.oauth2.redirect.uri}")
    public String naverOauth2RedirectUri;

    @Value("${naver.oauth2.request.uri}")
    public String naverOauth2RequestUri;

    @Value("${naver.oauth2.token.uri}")
    public String naverOauth2TokenUri;

    @Value("${naver.oauth2.userinfo.uri}")
    public String naverOauth2UserinfoUri;

    @Value("${naver.oauth2.revoke.uri}")
    public String naverOauth2RevokeUri;

    @Value("${naver.clientId}")
    public String naverClientId;

    @Value("${naver.clientSecret}")
    public String naverClientSecret;

}
