package com.system.lms.fo.auth;

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

    private static final String BASE_KEY = "thisisdummykeythisisdummykeythisisdummykeythisisdummykeythisisdummykey";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private Key createKey() {
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SIGNATURE_ALGORITHM.getJcaName());

        return signingKey;
    }

    public String createJwt(Map<String, Object> customClaims) {
        Map<String, Object> headerMap = Map.of(
                "typ", "JWT",
                "alg", "HS256"
        );

        Date expireTime = new Date(System.currentTimeMillis() + 60000 * 1000); // 1000분 후 만료

        return Jwts.builder()
                .setHeader(headerMap)
                .setClaims(customClaims)
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

            log.debug("email : {}", claims.get("email"));
            return true;
        } catch (ExpiredJwtException e) {
            log.info("Token expired");
        } catch (JwtException e) {
            log.warn("Invalid token");
        }

        return false;
    }

    public Claims getJwtClaims(String jwt) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Base64.getDecoder().decode(BASE_KEY))
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Token expired");
        } catch (JwtException e) {
            log.warn("Invalid token");
        }

        throw new RuntimeException("JWT claims 를 얻을 수 없습니다.");
    }
}