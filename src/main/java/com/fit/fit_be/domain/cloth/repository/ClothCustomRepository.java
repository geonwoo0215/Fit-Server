package com.fit.fit_be.domain.cloth.repository;

import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.model.ClothType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClothCustomRepository {
    Page<Cloth> findAllByType(Pageable pageable, Long memberId, ClothType clothType);

}
