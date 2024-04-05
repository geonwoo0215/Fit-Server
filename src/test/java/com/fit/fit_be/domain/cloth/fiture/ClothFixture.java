package com.fit.fit_be.domain.cloth.fiture;

import com.fit.fit_be.domain.cloth.dto.request.SaveClothRequest;
import com.fit.fit_be.domain.cloth.dto.request.UpdateClothRequest;
import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.model.ClothType;
import com.fit.fit_be.domain.member.model.Member;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClothFixture {

    private static final String INFORMATION = "슈프림 티셔츠";
    private static final String SIZE_M = "M";
    private static final String SIZE_L = "L";

    public static Cloth createCloth(Member member) {
        return Cloth.builder()
                .member(member)
                .type(ClothType.TOP)
                .information(INFORMATION)
                .size(SIZE_M)
                .build();
    }

    public static List<Cloth> createCloths(Member member, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> ClothFixture.createCloth(member))
                .collect(Collectors.toList());
    }

    public static SaveClothRequest createSaveClothRequest() {
        return SaveClothRequest.builder()
                .type(ClothType.TOP)
                .information(INFORMATION)
                .size(SIZE_M)
                .build();
    }

    public static UpdateClothRequest createUpdateClothRequest() {
        return UpdateClothRequest.builder()
                .type(ClothType.BOTTOM)
                .information(INFORMATION)
                .size(SIZE_L)
                .build();
    }
}
