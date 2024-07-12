package com.example.final_project_java.charger.dto.response;

import com.example.final_project_java.charger.Entity.ChargingStation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Getter @Setter @ToString
public class pageResponseDTO {

    private int startPage;
    private int endPage;
    private int currentPage;

    private boolean prev, next;

    private int totalCount;

    // 한 페이지에 배치할 페이지 버튼 수
    private static final int PAGE_COUNT = 10;

    public pageResponseDTO(Page<ChargingStation> pageData) {
        this.totalCount = (int) pageData.getTotalElements();
        this.currentPage = pageData.getPageable().getPageNumber() + 1;
        this.endPage = (int) (Math.ceil((double) currentPage/ PAGE_COUNT) * PAGE_COUNT);
        this.startPage = endPage - PAGE_COUNT + 1;

        int realEnd = pageData.getTotalPages();
        if (realEnd < this.endPage) this.endPage = realEnd;

        this.prev = startPage > 1;
        this.next = endPage < realEnd;
    }

}