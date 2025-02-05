package com.system.lms.fo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NavbarController {

    @GetMapping("/intro")
    public String intro() { return "fo/intro"; }

    @GetMapping("/courses")
    public String courses() { return "fo/courses"; }

    @GetMapping("/about")
    public String about() { return "fo/about"; }

    @GetMapping("/login")
    public String login() { return "fo/login"; }
}
