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
}
