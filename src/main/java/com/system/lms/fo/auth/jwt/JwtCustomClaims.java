package com.system.lms.fo.auth.jwt;

import java.util.Map;

public record JwtCustomClaims(
        String snsType,
        String email,
        String accessToken
) {
    public Map<String, Object> parseToMap() {
        return Map.of(
                "snsType", snsType,
                "email", email,
                "accessToken", accessToken
        );
    }
}