package com.system.lms.fo.service;

import com.system.lms.fo.mapper.FaqMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqService {

    public static final int pagePerCount = 10;

    private final FaqMapper faqMapper;

    @Transactional
    public void viewFaqList(String pageStr, String sizeStr, Model model) {
        int page = (pageStr != null) ? Integer.parseInt(pageStr) : 1;
        int size = (sizeStr != null) ? Integer.parseInt(sizeStr) : pagePerCount;
        int offset = (page - 1) * size;

        Map<String, Object> params = Map.of(
            "limit", pagePerCount,
            "offset", offset
        );

        Long totalCount = faqMapper.selectFaqCount();
        List<Map<String, Object>> faqList = faqMapper.selectFaqByPaging(params);

        int totalPages = (int) Math.ceil((double) totalCount / pagePerCount);

        model.addAttribute("faqList", faqList);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pagePerCount);
    }
}
