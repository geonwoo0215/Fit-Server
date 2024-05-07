package com.fit.fit_be.global.client.dto;

import lombok.Getter;

@Getter
public class Body {
    private String dataType;
    private Items items;
    private int pageNo;
    private int numOfRows;
    private int totalCount;

}