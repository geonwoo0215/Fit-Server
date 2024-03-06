package com.fit.fit_be.domain.cloth.service;

import com.fit.fit_be.domain.cloth.dto.request.SaveClothRequest;
import com.fit.fit_be.domain.cloth.dto.request.UpdateClothRequest;
import com.fit.fit_be.domain.cloth.dto.response.ClothResponse;
import com.fit.fit_be.domain.cloth.exception.ClothNotFoundException;
import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.model.ClothType;
import com.fit.fit_be.domain.cloth.repository.ClothRepository;
import com.fit.fit_be.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClothService {

    private final ClothRepository clothRepository;

    @Transactional
    public Long save(Member member, SaveClothRequest saveClothRequest) {
        Cloth cloth = saveClothRequest.toCloth(member);
        Cloth saveCloth = clothRepository.save(cloth);
        return saveCloth.getId();
    }

    public ClothResponse findById(Long clothId) {
        Cloth cloth = clothRepository.findById(clothId)
                .orElseThrow(() -> new ClothNotFoundException(clothId));
        ClothResponse clothResponse = cloth.toClothResponse();
        return clothResponse;
    }

    public List<ClothResponse> findAll(String type, Long memberId, Pageable pageable) {
        if (Objects.equals(type, "")) {
            Page<Cloth> cloths = clothRepository.findAllByMember_Id(memberId, pageable);
            List<ClothResponse> boardResponseList = cloths.stream()
                    .map(Cloth::toClothResponse)
                    .toList();
            return boardResponseList;
        }
        ClothType clothType = ClothType.of(type);
        Page<Cloth> cloths = clothRepository.findAllByMember_IdAndType(memberId, clothType, pageable);
        List<ClothResponse> boardResponseList = cloths.stream()
                .map(Cloth::toClothResponse)
                .toList();
        return boardResponseList;
    }

    @Transactional
    public Long update(Long clothId, UpdateClothRequest updateClothRequest) {
        Cloth cloth = clothRepository.findById(clothId)
                .orElseThrow(() -> new ClothNotFoundException(clothId));
        cloth.updateCloth(updateClothRequest);
        return cloth.getId();
    }

    @Transactional
    public void delete(Long clothId) {
        clothRepository.deleteById(clothId);
    }


}
