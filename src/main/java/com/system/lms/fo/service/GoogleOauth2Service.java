package com.system.lms.fo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.json.Json;
import com.system.lms.fo.common.Env;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOauth2Service {

    private final Env env;
    private final RestTemplate restTemplate;

    public String loginGoogle(String code) {
        String accessToken = requestGoogleAccessToken(code);
        log.debug("accessToken : {}", accessToken);

        // 로그아웃 개발하고 주석 해제
        JsonNode userResourceNode = getUserResource(accessToken);
//        JsonNode userInfoNode = getUserInfo(accessToken);

        log.debug("userResourceNode : {}", userResourceNode);
//        log.debug("userInfoNode : {}", userInfoNode);

        String id = userResourceNode.get("id").asText();
        String email = userResourceNode.get("email").asText();
        String nickname = userResourceNode.get("name").asText();

        log.debug("id : {}", id);
        log.debug("email : {}", email);
        log.debug("nickname : {}", nickname);

        return accessToken;
    }

    private String requestGoogleAccessToken(String code) {
        try {
            String clientId = env.googleClientId; // 클라이언트 ID
            String clientSecret = env.googleClientSecret; // 클라이언트 보안 패스워드
            String tokenUri = "https://oauth2.googleapis.com/token";

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
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private JsonNode getUserResource(String accessToken) {
        String resourceUri = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);

        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }

    private JsonNode getUserInfo(String accessToken) {
        String userInfoUri = "https://people.googleapis.com/v1/people/me?personFields=names,emailAddresses,photos,phoneNumbers";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);

        return restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }

    public void removeAccessToken(String accessToken) {
        try {
            String tokenUri = UriComponentsBuilder.fromUriString("https://oauth2.googleapis.com/revoke")
                    .queryParam("token", accessToken)
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity entity = new HttpEntity(headers);

            ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);

            log.info("logout res node : {}", responseNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
