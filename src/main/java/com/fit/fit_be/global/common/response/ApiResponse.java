package com.fit.fit_be.global.common.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private T data;

    public ApiResponse(T data) {
        this.data = data;
    }
}
