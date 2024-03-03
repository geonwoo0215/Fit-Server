package com.fit.fit_be.domain.image.model;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String imageUrl;

    public Image(Board board, String imageUrl) {
        this.board = board;
        this.imageUrl = imageUrl;
    }
}
