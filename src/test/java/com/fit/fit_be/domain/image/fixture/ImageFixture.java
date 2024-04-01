package com.fit.fit_be.domain.image.fixture;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.image.model.Image;

public class ImageFixture {

    private static final String IMAGE_URL = "imageUrl";

    public static Image createImage(Board board) {
        return Image.of(board, IMAGE_URL);
    }

}
