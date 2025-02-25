package com.system.lms.fo.auth.sns;

import com.system.lms.fo.common.CommonVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KakaoOauth2Controller {

    private final CommonVO commonVO;
    private final KakaoOauth2Service kakaoOauth2Service;

    @GetMapping("/login")
    public void requestKakaoLogin(HttpServletResponse response) throws IOException {
        String url = UriComponentsBuilder.fromUriString(commonVO.kakaoOauth2RequestUri)
                .queryParam("response_type", "code")
                .queryParam("client_id", commonVO.kakaoClientId)
                .queryParam("redirect_uri", commonVO.kakaoRedirectUri)
                .toUriString();

        response.sendRedirect(url);
    }

    @GetMapping("/login/callback")
    public String callbackKakao(HttpServletRequest request, HttpServletResponse response) {
        kakaoOauth2Service.loginKakao(request, response);

        return "redirect:/";
    }
}
