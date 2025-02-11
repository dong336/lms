package com.system.lms.fo.util;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class PaginationUtil {

    private final int itemsPerPage;
    private final int pageButtonCount;

    public Map<String, Integer> calculatePagination(int currentPage, int totalCount) {
        int totalPages = (int) Math.ceil((double) totalCount / itemsPerPage);
        int startPage = ((currentPage - 1) / pageButtonCount) * pageButtonCount + 1;
        int endPage = Math.min(((currentPage - 1) / pageButtonCount + 1) * pageButtonCount, totalPages);

        return Map.of(
                "startPage", startPage,
                "endPage", endPage,
                "totalCount", totalCount,
                "totalPages", totalPages,
                "currentPage", currentPage,
                "pageSize", itemsPerPage
        );
    }
}