package com.system.lms.fo.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Env {
    @Value("${google.clientId}")
    public String googleClientId;

    @Value("${google.clientSecret}")
    public String googleClientSecret;
}
