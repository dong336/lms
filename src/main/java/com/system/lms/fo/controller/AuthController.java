package com.system.lms.fo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/google/callback")
    public String getGoogle(@RequestParam Map<String, Object> request) {
        log.info("req :: {}", request);

        return "fo/google_callback";
    }
}
