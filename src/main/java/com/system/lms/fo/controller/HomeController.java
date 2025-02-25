package com.system.lms.fo.controller;

import com.system.lms.fo.common.CommonVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CommonVO commonVO;

    @GetMapping("/intro")
    public String intro() { return "fo/intro"; }

    @GetMapping("/courses")
    public String courses() { return "fo/courses"; }

    @GetMapping("/login")
    public String login() { return "fo/login"; }

    @GetMapping("/")
    public String blank() { return "fo/index"; }

    @GetMapping("/index")
    public String index() { return "redirect:/"; }

    @GetMapping("/home")
    public String home() {
        return "redirect:/";
    }
}
