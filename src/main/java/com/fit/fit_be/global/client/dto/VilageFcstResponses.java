package com.fit.fit_be.global.client.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class VilageFcstResponses {

    private String dataType;

    private List<VilageFcstResponse> items;

    private Integer pageNo;

    private Integer numOfRows;

    private Integer totalCount;

    @Builder
    public VilageFcstResponses(String dataType, List<VilageFcstResponse> items, Integer pageNo, Integer numOfRows, Integer totalCount) {
        this.dataType = dataType;
        this.items = items;
        this.pageNo = pageNo;
        this.numOfRows = numOfRows;
        this.totalCount = totalCount;
    }
}
