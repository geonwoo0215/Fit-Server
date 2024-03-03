package com.fit.fit_be.domain.image.repository;

import com.fit.fit_be.domain.image.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
