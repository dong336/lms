package com.system.lms.fo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QnaController {

    @GetMapping("/about/qnaList")
    public String qna() { return "fo/about/qnaList"; }

    @GetMapping("/about/qnaForm")
    public String qnaForm() { return "fo/about/qnaForm"; }
}
