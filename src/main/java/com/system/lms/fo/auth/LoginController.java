package com.system.lms.fo.auth;

import com.system.lms.fo.client.EmailSender;
import com.system.lms.fo.client.EmailValidator;
import jakarta.servlet.http.Cookie;
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
import java.util.Random;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final JwtHelper jwtHelper;

    private final EmailValidator emailValidator;
    private final EmailSender emailSender;

    private final LoginService loginService;

    @GetMapping("/auth")
    public String auth(@RequestParam Map<String, Object> param, HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) param.get("email");
        String[] parts = email.split("@");

        boolean isValid = emailValidator.isValidDomain(parts[1]);

        if (isValid) {
            Random random = new Random();
            int pinNumber = 100000 + random.nextInt(900000);

            String body = "안녕하세요. 회원인증 코드를 발송합니다.\n" + pinNumber;

            emailSender.sendMessage(email, "회원 인증코드입니다.", body);

            session.setAttribute("pinNumber", pinNumber + "");
            session.setAttribute("email", email);

            return "fo/login/auth";
        } else {
            redirectAttributes.addFlashAttribute("isInvalidEmail", true);

            return "redirect:/login";
        }
    }

    @GetMapping("/validate/{combinedNumber}")
    public String validate(@PathVariable String combinedNumber, HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model) {
        String pinNumber = (String) session.getAttribute("pinNumber");
        String email = (String) session.getAttribute("email");

        if (pinNumber.equals(combinedNumber)) {
            String jwtToken = jwtHelper.createJwt(Map.of("email", email));

            Cookie cookie = new Cookie("jwtToken", jwtToken);

            cookie.setMaxAge(30000);
            cookie.setPath("/");

            response.addCookie(cookie);

            return "redirect:/";
        } else {
            model.addAttribute("isFailPinNum", true);

            session.invalidate();

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

            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false); // 세션이 없다면 null
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    // 쿠키 삭제 처리
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    cookie.setSecure(false);

                    response.addCookie(cookie);
                }
            }
        }

        return "redirect:/";
    }
}
