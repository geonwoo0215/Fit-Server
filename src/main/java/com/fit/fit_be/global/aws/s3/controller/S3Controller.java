package com.fit.fit_be.global.aws.s3.controller;

import com.fit.fit_be.global.aws.s3.service.S3Service;
import com.fit.fit_be.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Profile("!test")
public class S3Controller {

    private final S3Service s3service;

    @PostMapping("/file/multiparty-files")
    public ResponseEntity<ApiResponse<List<String>>> multipleFilesUpload
            (
                    @RequestPart(name = "multipartFiles") List<MultipartFile> multipartFiles
            ) {

        List<String> uploadUrls = s3service.putObject(multipartFiles);
        return new ResponseEntity<>(new ApiResponse<>(uploadUrls), HttpStatus.CREATED);
    }
}

