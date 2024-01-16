package com.fit.fit_be.domain.board.service;

import com.fit.fit_be.domain.board.dto.request.SaveBoardRequest;
import com.fit.fit_be.domain.board.dto.request.UpdateBoardRequest;
import com.fit.fit_be.domain.board.dto.response.BoardResponse;
import com.fit.fit_be.domain.board.exception.BoardNotFoundException;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.boardcloth.model.BoardCloth;
import com.fit.fit_be.domain.boardcloth.repository.BoardClothRepository;
import com.fit.fit_be.domain.cloth.exception.ClothNotFoundException;
import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.repository.ClothRepository;
import com.fit.fit_be.domain.image.model.Image;
import com.fit.fit_be.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardClothRepository boardClothRepository;
    private final ClothRepository clothRepository;

    @Transactional
    public Long save(Member member, SaveBoardRequest saveBoardRequest) {
        Board board = saveBoardRequest.toBoard(member);
        saveBoardRequest.getImageUrls().forEach(
                imageUrl -> {
                    Image image = new Image(board, imageUrl);
                    board.addImage(image);
                }
        );
        saveBoardRequest.getClothAppropriates().forEach((clothId, isAppropriate) -> {
            Cloth cloth = clothRepository.findById(clothId)
                    .orElseThrow(() -> new ClothNotFoundException(clothId));
            BoardCloth boardCloth = new BoardCloth(board, cloth, isAppropriate);
            board.addBoardCloth(boardCloth);
        });
        Board saveBoard = boardRepository.save(board);
        return saveBoard.getId();
    }

    public BoardResponse findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException(id));
        BoardResponse boardResponse = board.toBoardResponse();
        return boardResponse;
    }

    @Transactional
    public Long update(Long boardId, UpdateBoardRequest updateBoardRequest) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));
        board.updateBoard(updateBoardRequest);
        return board.getId();
    }

    @Transactional
    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }

}
