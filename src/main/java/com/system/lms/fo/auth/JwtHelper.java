package com.system.lms.fo.auth;

import com.system.lms.fo.common.Env;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHelper {

    private final Env env;
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private Key createKey() {
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(env.jwtKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SIGNATURE_ALGORITHM.getJcaName());

        return signingKey;
    }

    public String createJwt(JwtCustomClaims jwtCustomClaims) {
        Map<String, Object> headerMap = Map.of(
                "typ", "JWT",
                "alg", "HS256"
        );

        Map<String, Object> claims = Map.of(
                "snsType", jwtCustomClaims.snsType(),
                "email", jwtCustomClaims.email(),
                "accessToken", jwtCustomClaims.accessToken()
        );

        Date expireTime = new Date(System.currentTimeMillis() + 60000 * 1000); // 1000분 후 만료

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
                    .setSigningKey(Base64.getDecoder().decode(env.jwtKey))
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

    public JwtCustomClaims getJwtClaims(String jwt) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Base64.getDecoder().decode(env.jwtKey))
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            return new JwtCustomClaims(
                    (String) claims.get("snsType"),
                    (String) claims.get("email"),
                    (String) claims.get("accessToken")
            );
        } catch (ExpiredJwtException e) {
            log.info("Token expired");
        } catch (JwtException e) {
            log.warn("Invalid token");
        }
        throw new RuntimeException("JWT claims 를 얻을 수 없습니다.");
    }
}