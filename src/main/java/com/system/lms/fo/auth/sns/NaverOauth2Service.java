package com.system.lms.fo.auth.sns;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.json.Json;
import com.system.lms.fo.auth.jwt.JwtCustomClaims;
import com.system.lms.fo.auth.jwt.JwtHelper;
import com.system.lms.fo.common.CommonVO;
import com.system.lms.fo.common.CookieBuilder;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOauth2Service {

    private final CommonVO commonVO;
    private final RestTemplate restTemplate;
    private final JwtHelper jwtHelper;

    public void loginNaver(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        log.debug("code : {}", code);

        String accessToken = requestNaverAccessToken(code);
        log.debug("accessToken : {}", accessToken);

        JsonNode userinfoNode = getUserinfo(accessToken);
        log.debug("userinfoNode : {}", userinfoNode);

        String email = userinfoNode.path("response").path("email").asText();
        log.debug("email : {}", email);

        JwtCustomClaims customClaims = new JwtCustomClaims(SnsType.NAVER.getValue(), email, accessToken);

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
                .value(SnsType.NAVER.getValue())
                .maxAge(315360000)
                .path("/")
//                .secure(true) TODO : TLS 적용 후에 주석 제거
                .build();
        response.addCookie(lastLoginSnsCookie);
    }

    private String requestNaverAccessToken(String code) {
        try {
            String clientId = commonVO.naverClientId; // 클라이언트 ID
            String clientSecret = commonVO.naverClientSecret; // 클라이언트 보안 패스워드
            String tokenUri = commonVO.naverOauth2TokenUri;

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", commonVO.naverOauth2RedirectUri);
            params.add("grant_type", "authorization_code");
            params.add("state", "test");  // CSRF 방지 인증

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

    private JsonNode getUserinfo(String accessToken) {
        try {
            String userinfoUri = commonVO.naverOauth2UserinfoUri;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity entity = new HttpEntity(headers);

            return restTemplate.exchange(userinfoUri, HttpMethod.GET, entity, JsonNode.class).getBody();
        } catch (Exception e) {
            String className = e.getStackTrace()[0].getClassName();
            String methodName = e.getStackTrace()[0].getMethodName();

            throw new RuntimeException(className + "." + methodName + "예외 발생: " + e.getMessage(), e);
        }
    }

    public void removeAccessToken(String accessToken) {
        try {
            String revokeTokenUri = UriComponentsBuilder.fromUriString(commonVO.naverOauth2RevokeUri)
                    .queryParam("grant_type", "delete")
                    .queryParam("client_id", commonVO.naverClientId)
                    .queryParam("client_secret", commonVO.naverClientSecret)
                    .queryParam("access_token", accessToken)
                    .queryParam("service_provider", SnsType.NAVER.getValue().toUpperCase())
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity entity = new HttpEntity(headers);

            ResponseEntity<JsonNode> responseNode = restTemplate.exchange(revokeTokenUri, HttpMethod.GET, entity, JsonNode.class);

            log.debug("responseNode : {}", responseNode);
        } catch (Exception e) {
            String className = e.getStackTrace()[0].getClassName();
            String methodName = e.getStackTrace()[0].getMethodName();

            throw new RuntimeException(className + "." + methodName + "예외 발생 : " + e.getMessage(), e);
        }
    }
}
