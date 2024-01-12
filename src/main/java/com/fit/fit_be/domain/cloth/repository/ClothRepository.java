package com.fit.fit_be.domain.cloth.repository;

import com.fit.fit_be.domain.cloth.model.Cloth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothRepository extends JpaRepository<Cloth, Long> {
}
