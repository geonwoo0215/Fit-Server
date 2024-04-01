package com.fit.fit_be.domain.boardcloth.model;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.cloth.model.Cloth;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCloth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cloth cloth;

    private boolean appropriate;

    private BoardCloth(Board board, Cloth cloth, boolean appropriate) {
        this.board = board;
        this.cloth = cloth;
        this.appropriate = appropriate;
    }

    public static BoardCloth of(Board board, Cloth cloth, boolean appropriate) {
        return new BoardCloth(board, cloth, appropriate);
    }

}
