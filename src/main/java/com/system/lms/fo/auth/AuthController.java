package com.system.lms.fo.auth;

import com.system.lms.fo.auth.jwt.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String auth(@RequestParam Map<String, Object> param, HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) param.get("email");

        boolean isSent = authService.sendAuthCode(email, session);

        if (isSent) {
            return "fo/login/auth"; // 인증 페이지로 이동
        } else {
            redirectAttributes.addFlashAttribute("isInvalidEmail", true);
            return "redirect:/login"; // 이메일이 유효하지 않을 경우 리다이렉트
        }
    }

    @GetMapping("/validate/{combinedNumber}")
    public String validate(@PathVariable String combinedNumber, HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model) {
        boolean isPinValid = authService.validatePinAndCreateJwt(combinedNumber, session, response);

        if (isPinValid) {
            return "redirect:/"; // 핀 번호가 맞으면 메인 페이지로 리다이렉트
        } else {
            model.addAttribute("isFailPinNum", true);

            // 세션 무효화 및 쿠키 삭제
            authService.invalidateSessionAndDeleteCookies(session, request, response);

            return "redirect:/login"; // 실패하면 로그인 페이지로 리다이렉트
        }
    }

    // logout 은 공통으로 처리하며 claims 를 검사하여 분기 처리
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);

        return "redirect:/";
    }
}
