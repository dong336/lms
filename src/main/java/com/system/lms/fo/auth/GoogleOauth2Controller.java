package com.system.lms.fo.auth;

import com.system.lms.fo.common.Env;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/auth/google")
@RequiredArgsConstructor
public class GoogleOauth2Controller {

    private final Env env;
    private final JwtHelper jwtHelper;
    private final GoogleOauth2Service googleOauth2Service;

    @GetMapping("/login")
    public void requestGoogleLogin(HttpServletResponse response, @RequestParam Map<String, Object> request) throws IOException {
        String redirectUri = env.googleRedirectUri;

        String url = UriComponentsBuilder.fromUriString(env.googleOauth2RequestUri)
                .queryParam("client_id", env.googleClientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope",
                        env.googleReadScopeEmailUri + " " +
                        env.googleReadScopeProfileUri)
                /*
                 * 개인정보 응답 받기 옵션
                 */
//                        " https://www.googleapis.com/auth/user.birthday.read"
//                        " https://www.googleapis.com/auth/user.gender.read"
                .queryParam("access_type", "offline")
                .queryParam("prompt", "select_account")
                .build()
                .toUriString();

        response.sendRedirect(url);
    }

    @GetMapping("/login/callback")
    public String callbackGoogle(HttpServletResponse response, @RequestParam Map<String, Object> request) {
        log.debug("req : {}", request);

        String code = (String) request.get("code");

        log.debug("code : {}", code);

        String accessToken = googleOauth2Service.loginGoogle(code);

        JwtCustomClaims customClaims = new JwtCustomClaims("google", "", accessToken);

        String jwtToken = jwtHelper.createJwt(customClaims);

        Cookie cookie = new Cookie("jwtToken", jwtToken);

        cookie.setMaxAge(30000);
        cookie.setPath("/");

        response.addCookie(cookie);

        return "redirect:/";
    }
}

