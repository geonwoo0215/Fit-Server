package com.fit.fit_be.global.client.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class VilageFcstResponses {

    private String dataType;

    private List<VilageFcstResponse> items;

    private Integer pageNo;

    private Integer numOfRows;

    private Integer totalCount;

}
