package com.system.lms.fo.controller;

import com.system.lms.fo.common.CommonVO;
import com.system.lms.fo.service.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    @GetMapping("/about/faq")
    public String faq(
            @RequestParam(value = "page", required = false) String page,
            Model model) {
        faqService.viewFaqList(page, model);

        return "fo/about/faq";
    }
}