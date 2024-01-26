package com.fit.fit_be.domain.cloth.repository;

import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.model.ClothType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ClothRepository extends JpaRepository<Cloth, Long> {

    Page<Cloth> findAllByMember_Id(@Param("memberId") Long memberId, Pageable pageable);

    Page<Cloth> findAllByMember_IdAndType(Long member_id, ClothType type, Pageable pageable);

}
