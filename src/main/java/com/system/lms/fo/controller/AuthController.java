package com.system.lms.fo.controller;

import com.system.lms.fo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@RequestMapping("/auth")
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/google/callback")
    public String callbackGoogle(@RequestParam Map<String, Object> request) {
        log.info("req : {}", request);

        String code = (String) request.get("code");

        authService.loginGoogle(code);

        return "fo/google_callback";
    }
}
