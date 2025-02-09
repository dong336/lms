package com.system.lms.fo.auth.sns;

import com.fasterxml.jackson.databind.JsonNode;
import com.system.lms.fo.auth.jwt.JwtCustomClaims;
import com.system.lms.fo.auth.jwt.JwtHelper;
import com.system.lms.fo.common.CookieBuilder;
import com.system.lms.fo.common.Env;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOauth2Service {

    private final Env env;

    private final RestTemplate restTemplate;
    private final JwtHelper jwtHelper;

    public void loginGoogle(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = requestGoogleAccessToken(request.getParameter("code"));
        log.debug("accessToken : {}", accessToken);

        JsonNode userResourceNode = getUserResource(accessToken);

        log.debug("userResourceNode : {}", userResourceNode);

        String id = userResourceNode.get("id").asText();
        String email = userResourceNode.get("email").asText();
        String nickname = userResourceNode.get("name").asText();

        log.debug("id : {}", id);
        log.debug("email : {}", email);
        log.debug("nickname : {}", nickname);

        JwtCustomClaims customClaims = new JwtCustomClaims(SnsType.GOOGLE.getValue(), email, accessToken);

        String jwtToken = jwtHelper.createJwt(customClaims);

        Cookie jwtCookie = CookieBuilder.builder()
                .name("jwtToken")
                .value(jwtToken)
                .maxAge(30000)
                .path("/")
                .httpOnly(true)
                .build();
        response.addCookie(jwtCookie);

        Cookie lastLoginSnsCookie = CookieBuilder.builder()
                .name("lastLoginSns")
                .value(SnsType.GOOGLE.getValue())
                .maxAge(315360000)
                .path("/")
//                .secure(true) TODO : TLS 적용 후에 주석 제거
                .build();
        response.addCookie(lastLoginSnsCookie);
    }

    private String requestGoogleAccessToken(String code) {
        try {
            String clientId = env.googleClientId; // 클라이언트 ID
            String clientSecret = env.googleClientSecret; // 클라이언트 보안 패스워드
            String tokenUri = env.googleOauth2TokenUri;

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", env.googleRedirectUri);
            params.add("grant_type", "authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity entity = new HttpEntity(params, headers);

            ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
            JsonNode accessTokenNode = responseNode.getBody();

            return accessTokenNode.get("access_token").asText();
        } catch (RuntimeException e) {
            String className = e.getStackTrace()[0].getClassName();
            String methodName = e.getStackTrace()[0].getMethodName();

            throw new RuntimeException(className + "." + methodName + "에서 예외 발생 : " + e.getMessage(), e);
        }
    }

    private JsonNode getUserResource(String accessToken) {
        try {
            String resourceUri = env.googleOauth2UserinfoUri;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity entity = new HttpEntity(headers);

            return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
        } catch (RuntimeException e) {
            String className = e.getStackTrace()[0].getClassName();
            String methodName = e.getStackTrace()[0].getMethodName();

            throw new RuntimeException(className + "." + methodName + "에서 예외 발생 : " + e.getMessage(), e);
        }
    }

    public void removeAccessToken(String accessToken) {
        try {
            String tokenUri = UriComponentsBuilder.fromUriString(env.googleOauth2RevokeUri)
                    .queryParam("token", accessToken)
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity entity = new HttpEntity(headers);

            ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);

            log.debug("logout res node : {}", responseNode);
        } catch (RuntimeException e) {
            String className = e.getStackTrace()[0].getClassName();
            String methodName = e.getStackTrace()[0].getMethodName();

            throw new RuntimeException(className + "." + methodName + "에서 예외 발생 : " + e.getMessage(), e);
        }
    }
}
