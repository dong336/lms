package com.system.lms.fo.service;

import com.system.lms.fo.auth.jwt.JwtCustomClaims;
import com.system.lms.fo.auth.jwt.JwtHelper;
import com.system.lms.fo.mapper.QnaMapper;
import com.system.lms.fo.util.PaginationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class QnaService {

    public static final int PAGE_BUTTON_COUNT = 5;
    public static final int ITEMS_PER_PAGE = 10;

    private final JwtHelper jwtHelper;
    private final QnaMapper qnaMapper;

    @Transactional
    public void viewQnaList(String page, Model model) {
        int currentPage = (page != null) ? Integer.parseInt(page) : 1;
        int offset = (currentPage - 1) * ITEMS_PER_PAGE;

        Map<String, Object> params = Map.of(
                "limit", ITEMS_PER_PAGE,
                "offset", offset
        );

        int totalCount = qnaMapper.selectQnaCount();
        List<Map<String, Object>> qnaList = qnaMapper.selectQnaByPaging(params);

        PaginationUtil paginationUtil = new PaginationUtil(ITEMS_PER_PAGE, PAGE_BUTTON_COUNT);
        Map<String, Integer> pagination = paginationUtil.calculatePagination(currentPage, totalCount);

        model.addAttribute("qnaList", qnaList);
        model.addAttribute("pagination", pagination);
    }

    @Transactional
    public void insertQnaForm(HttpServletRequest request) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String jwtToken = (String) request.getAttribute("jwtToken");

        JwtCustomClaims jwtCustomClaims = jwtHelper.getJwtClaims(jwtToken);
        String email = jwtCustomClaims.email();

        Map<String, Object> params = Map.of(
                "title", title,
                "content", content,
                "writer", email
        );

        qnaMapper.insertQna(params);
    }
}
