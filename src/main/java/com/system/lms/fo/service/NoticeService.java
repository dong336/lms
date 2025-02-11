package com.system.lms.fo.service;

import com.system.lms.fo.mapper.NoticeMapper;
import com.system.lms.fo.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    public static final int PAGE_BUTTON_COUNT = 5;
    public static final int ITEMS_PER_PAGE = 10;

    private final NoticeMapper noticeMapper;

    @Transactional
    public void viewNoticeList(String page, Model model) {
        int currentPage = (page != null) ? Integer.parseInt(page) : 1;
        int offset = (currentPage - 1) * ITEMS_PER_PAGE;

        Map<String, Object> params = Map.of(
            "limit", ITEMS_PER_PAGE,
            "offset", offset
        );

        int totalCount = noticeMapper.selectNoticeCount();
        List<Map<String, Object>> noticeList = noticeMapper.selectNoticeByPaging(params);

        PaginationUtil paginationUtil = new PaginationUtil(ITEMS_PER_PAGE, PAGE_BUTTON_COUNT);
        Map<String, Integer> pagination = paginationUtil.calculatePagination(currentPage, totalCount);

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("pagination", pagination);
    }
}
