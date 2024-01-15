package com.fit.fit_be.global.aws.s3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Profile("!test")
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public List<String> putObject(List<MultipartFile> multipartFiles) {

        List<String> uploadedUrls = new ArrayList<>();

        multipartFiles
                .forEach(file -> {
                    String objectKey = "expense/" + UUID.randomUUID().toString().substring(0, 8) + file.getOriginalFilename();
                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .contentType(file.getContentType())
                            .contentLength(file.getSize())
                            .build();
                    try {
                        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
                        uploadedUrls.add(objectKey);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        return uploadedUrls;
    }


}
