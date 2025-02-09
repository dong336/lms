package com.system.lms.fo.auth;

import com.system.lms.fo.auth.jwt.JwtCustomClaims;
import com.system.lms.fo.auth.jwt.JwtHelper;
import com.system.lms.fo.auth.sns.GoogleOauth2Service;
import com.system.lms.fo.auth.sns.KakaoOauth2Service;
import com.system.lms.fo.client.EmailSender;
import com.system.lms.fo.common.CookieBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtHelper jwtHelper;
    private final EmailSender emailSender;

    private final GoogleOauth2Service googleOauth2Service;
    private final KakaoOauth2Service kakaoOauth2Service;

    public boolean validatePinAndCreateJwt(String combinedNumber, HttpSession session, HttpServletResponse response) {
        String pinNumber = (String) session.getAttribute("pinNumber");

        if (pinNumber != null && pinNumber.equals(combinedNumber)) {
            String email = (String) session.getAttribute("email");
            String jwtToken = jwtHelper.createJwt(new JwtCustomClaims("none", email, ""));

            // JWT 토큰 쿠키 설정
            Cookie jwtCookie = CookieBuilder.builder()
                    .name("jwtToken")
                    .value(jwtToken)
                    .maxAge(30000)
                    .path("/")
                    .httpOnly(true)
                    .build();
            response.addCookie(jwtCookie);

            return true;
        }
        return false;
    }

    public void invalidateSessionAndDeleteCookies(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        // 세션 무효화
        session.invalidate();

        // 쿠키 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");

                    response.addCookie(cookie);
                }
            }
        }
    }

    public boolean sendAuthCode(String email, HttpSession session) {
        // 이메일 도메인 검증
        String[] parts = email.split("@");
        boolean isValid = emailSender.isValidDomain(parts[1]);

        if (!isValid) {
            return false; // 유효하지 않은 이메일
        }

        // 6자리 인증 코드 생성
        int pinNumber = 100000 + new Random().nextInt(900000);
        String body = "안녕하세요. 회원인증 코드를 발송합니다.\n" + pinNumber;

        // 이메일 전송
        emailSender.sendMessage(email, "회원 인증코드입니다.", body);

        // 세션에 인증 코드 저장
        session.setAttribute("pinNumber", String.valueOf(pinNumber));
        session.setAttribute("email", email);

        return true;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false); // 세션이 없다면 null
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "JSESSIONID":
                        // JSESSIONID 쿠키 삭제 처리
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        cookie.setHttpOnly(true);
                        cookie.setSecure(false);
                        response.addCookie(cookie);
                        break;

                    case "jwtToken":
                        String jwtToken = cookie.getValue();
                        JwtCustomClaims customClaims = jwtHelper.getJwtClaims(jwtToken);

                        String accessToken = customClaims.accessToken();
                        String snsType = customClaims.snsType();

                        switch (snsType) {
                            case "google":
                                googleOauth2Service.removeAccessToken(accessToken);
                                break;

                            case "kakao":
                                kakaoOauth2Service.removeAccessToken(accessToken);
                                break;

                            default:
                                break;
                        }

                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        cookie.setHttpOnly(true);
                        cookie.setSecure(false);
                        response.addCookie(cookie);
                        break;
                }
            }
        }
    }
}
