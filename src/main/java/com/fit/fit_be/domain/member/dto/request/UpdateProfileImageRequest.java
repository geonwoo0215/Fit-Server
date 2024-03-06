package com.fit.fit_be.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProfileImageRequest {

    private String profileImage;

    public UpdateProfileImageRequest(String profileImage) {
        this.profileImage = profileImage;
    }
}
