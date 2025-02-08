package com.system.lms.fo.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Env {

    @Value("${google.clientId}")
    public String googleClientId;

    @Value("${google.clientSecret}")
    public String googleClientSecret;

    @Value("${google.redirect.uri}")
    public String googleRedirectUri;

    @Value("${google.email.sender}")
    public String googleEmailSender;

    @Value("${google.app.password}")
    public String googleAppPassword;

    @Value("${jwt.base.key}")
    public String jwtKey;

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
}
