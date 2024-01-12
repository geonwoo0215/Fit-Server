package com.fit.fit_be.domain.boardcloth.model;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.cloth.model.Cloth;
import jakarta.persistence.*;

@Entity
public class BoardCloth {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cloth cloth;

    private boolean appropriate;

    public BoardCloth(Board board, Cloth cloth, boolean appropriate) {
        this.board = board;
        this.cloth = cloth;
        this.appropriate = appropriate;
    }
}
