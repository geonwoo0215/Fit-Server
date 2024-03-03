package com.fit.fit_be.domain.boardcloth.repository;

import com.fit.fit_be.domain.boardcloth.model.BoardCloth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardClothRepository extends JpaRepository<BoardCloth,Long> {
}
