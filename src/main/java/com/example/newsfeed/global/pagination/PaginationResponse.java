package com.example.newsfeed.global.pagination;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PaginationResponse<T> {

    private final List<T> data;
    private final PaginationInfo pagination;

    public PaginationResponse(Page<T> page) {
        this.data = page.getContent();
        this.pagination = PaginationInfo.builder()
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .size(page.getSize())
                .build();
    }

    @Getter
    private static class PaginationInfo {

        private final int currentPage;
        private final int totalPages;
        private final long totalElements;
        private final int size;

        @Builder
        public PaginationInfo(
                int currentPage,
                int totalPages,
                long totalElements,
                int size
        ) {
            this.currentPage = currentPage;
            this.totalPages = totalPages;
            this.totalElements = totalElements;
            this.size = size;
        }
    }
}
