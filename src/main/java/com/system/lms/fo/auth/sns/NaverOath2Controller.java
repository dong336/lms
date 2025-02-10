package com.system.lms.fo.auth.sns;

import com.system.lms.fo.common.CommonVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/auth/naver")
@RequiredArgsConstructor
public class NaverOath2Controller {

    private final CommonVO commonVO;
    private final NaverOauth2Service naverOauth2Service;

    @GetMapping("/login")
    public void reqeustNaverLogin(HttpServletResponse response) throws IOException {
        String uri = UriComponentsBuilder.fromUriString(commonVO.naverOauth2RequestUri)
                .queryParam("response_type", "code")
                .queryParam("client_id", commonVO.naverClientId)
                .queryParam("redirect_uri", commonVO.naverOauth2RedirectUri)
                .toUriString();

        response.sendRedirect(uri);
    }

    @GetMapping("/login/callback")
    public String callbackNaver(HttpServletRequest request, HttpServletResponse response) {
        naverOauth2Service.loginNaver(request, response);

        return "redirect:/";
    }
}
