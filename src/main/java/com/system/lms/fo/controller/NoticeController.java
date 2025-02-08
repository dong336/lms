package com.system.lms.fo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NoticeController {

    @GetMapping("/about/notice")
    public String notice() {
        return "fo/about/notice";
    }
}
