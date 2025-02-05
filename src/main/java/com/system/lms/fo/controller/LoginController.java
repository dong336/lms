package com.system.lms.fo.controller;

import com.system.lms.fo.client.EmailSender;
import com.system.lms.fo.client.EmailValidator;
import com.system.lms.fo.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final EmailValidator emailValidator;
    private final EmailSender emailSender;
    private final LoginService loginService;

    @GetMapping("/auth")
    public String auth(@RequestParam Map<String, Object> param, RedirectAttributes redirectAttributes) {
        String email = (String) param.get("email");
        String[] parts = email.split("@");

        boolean isValid = emailValidator.isValidDomain(parts[1]);

        if (isValid) {
            Random random = new Random();
            int randomNumber = 100000 + random.nextInt(900000);

            String body = "안녕하세요. 회원인증 코드를 발송합니다.\n" + randomNumber;

            emailSender.sendMessage(email, "회원 인증코드입니다.", body);
        } else {
            redirectAttributes.addFlashAttribute("isInvalidEmail", true);

            return "redirect:/login";
        }

        return "fo/login/auth";
    }
}
