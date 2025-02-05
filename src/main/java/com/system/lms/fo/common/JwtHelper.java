package com.system.lms.fo.common;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtHelper {

    private static final String BASE_KEY = "JWT_SECRET_KEY";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private Key createKey() {
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SIGNATURE_ALGORITHM.getJcaName());

        return signingKey;
    }

    public String createJwt(Map<String, Object> request) {
        Map<String, Object> headerMap = Map.of(
                "typ", "JWT",
                "alg", "HS256"
        );

        Map<String, Object> claims = Map.of(
                "name", request.get("memberName"),
                "id", request.get("memberId")
        );

        Date expireTime = new Date(System.currentTimeMillis() + 60 * 1000); // 1분 후 만료

        return Jwts.builder()
                .setHeader(headerMap)
                .setClaims(claims)
                .setExpiration(expireTime)
                .signWith(createKey(), SIGNATURE_ALGORITHM)
                .compact();
    }

    public Boolean checkJwt(String jwt) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Base64.getDecoder().decode(BASE_KEY))
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            log.debug("Id : {}", claims.get("id"));
            log.debug("Name : {}", claims.get("name"));
            return true;

        } catch (ExpiredJwtException e) {
            log.info("Token expired");
        } catch (JwtException e) {
            log.warn("Invalid token");
        }

        return false;
    }
}