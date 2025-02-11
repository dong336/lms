package com.system.lms.fo.controller;

import com.system.lms.fo.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/about/notice")
    public String notice(
            @RequestParam(value = "page", required = false) String page,
            Model model) {
        noticeService.viewNoticeList(page, model);

        return "fo/about/notice";
    }
}
