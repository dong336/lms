package com.system.lms.fo.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;

    public void loginGoogle(String code) {
        String accessToken = requestGoogleAccessToken(code);
          // 로그아웃 개발하고 주석 해제
//        JsonNode userResourceNode = getUserResource(accessToken);
//        log.info("userResourceNode : {}", userResourceNode);
//
//        String id = userResourceNode.get("id").asText();
//        String email = userResourceNode.get("email").asText();
//        String nickname = userResourceNode.get("name").asText();
//
//        log.info("id : {}", id);
//        log.info("email : {}", email);
//        log.info("nickname : {}", nickname);
    }

    private String requestGoogleAccessToken(String code) {
        try {
            String clientId = ""; // 클라이언트 ID
            String clientSecret = ""; // 클라이언트 보안 패스워드
            String redirectUri = "http://localhost:8080/auth/google/callback";
            String tokenUri = "https://oauth2.googleapis.com/token";

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectUri);
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
}
