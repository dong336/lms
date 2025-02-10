package com.system.lms.fo.auth.sns;

import com.system.lms.fo.common.CommonVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/auth/google")
@RequiredArgsConstructor
public class GoogleOauth2Controller {

    private final CommonVO commonVO;
    private final GoogleOauth2Service googleOauth2Service;

    @GetMapping("/login")
    public void requestGoogleLogin(HttpServletResponse response) throws IOException {
        String redirectUri = commonVO.googleRedirectUri;

        String url = UriComponentsBuilder.fromUriString(commonVO.googleOauth2RequestUri)
                .queryParam("client_id", commonVO.googleClientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope",
                        commonVO.googleReadScopeEmailUri + " " +
                        commonVO.googleReadScopeProfileUri)
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
    public String callbackGoogle(HttpServletRequest request, HttpServletResponse response) {
        googleOauth2Service.loginGoogle(request, response);

        return "redirect:/";
    }
}

