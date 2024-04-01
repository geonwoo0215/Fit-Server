package com.fit.fit_be.domain.boardcloth.fixture;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.boardcloth.model.BoardCloth;
import com.fit.fit_be.domain.cloth.model.Cloth;

public class BoardClothFixture {

    public static BoardCloth createBoardCloth(Board board, Cloth cloth) {
        return BoardCloth.of(board, cloth, true);
    }

}
