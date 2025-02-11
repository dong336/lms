package com.system.lms.fo.controller;

import com.system.lms.fo.service.QnaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    @GetMapping("/about/qna")
    public String qna(
            @RequestParam(value = "page", required = false) String page,
            Model model) {
        qnaService.viewQnaList(page, model);

        return "fo/about/qna";
    }

    @GetMapping("/about/qnaForm")
    public String qnaForm(HttpServletRequest request) {
        return "fo/about/qnaForm";
    }

    @PostMapping("/about/qnaForm")
    public String createQnaForm(HttpServletRequest request) {
        qnaService.insertQnaForm(request);

        return "redirect:/about/qna";
    }
}
