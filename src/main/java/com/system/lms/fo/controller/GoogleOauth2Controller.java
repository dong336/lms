package com.system.lms.fo.controller;

import com.system.lms.fo.common.Env;
import com.system.lms.fo.service.GoogleOauth2Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class GoogleOauth2Controller {

    private final Env env;
    private final GoogleOauth2Service googleOauth2Service;

    @GetMapping("/login/google")
    public void requestGoogleLogin(HttpServletResponse response, @RequestParam Map<String, Object> request) throws IOException {
        String redirectUri = GoogleOauth2Service.redirectUri;

        String url = UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/auth")
                .queryParam("client_id", env.googleClientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope",
                        "https://www.googleapis.com/auth/userinfo.profile" +
                        " https://www.googleapis.com/auth/userinfo.email")
//                        " https://www.googleapis.com/auth/user.birthday.read"
//                        " https://www.googleapis.com/auth/user.gender.read"
                .queryParam("access_type", "offline")
                .queryParam("prompt", "select_account")
                .build()
                .toUriString();

        response.sendRedirect(url);
    }

    @GetMapping("/login/google/callback")
    public String callbackGoogle(HttpServletResponse response, @RequestParam Map<String, Object> request) {
        log.debug("req : {}", request);

        String code = (String) request.get("code");

        log.debug("code : {}", code);

        String accessToken = googleOauth2Service.loginGoogle(code);

        Cookie cookie = new Cookie("accessToken", accessToken);

        cookie.setMaxAge(300);
        cookie.setPath("/");

        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/logout/google")
    public String requestGoogleLogout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    String accessToken = cookie.getValue();

                    cookie.setMaxAge(0);

                    log.debug("accessToken : {}", accessToken);

                    googleOauth2Service.removeAccessToken(accessToken);
                }
            }
        }

        return "redirect:/";
    }
}
