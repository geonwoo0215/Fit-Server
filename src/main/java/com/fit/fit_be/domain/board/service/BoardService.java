package com.fit.fit_be.domain.board.service;

import com.fit.fit_be.domain.board.dto.request.SaveBoardRequest;
import com.fit.fit_be.domain.board.dto.request.UpdateBoardRequest;
import com.fit.fit_be.domain.board.dto.response.BoardResponse;
import com.fit.fit_be.domain.board.exception.BoardNotFoundException;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.repository.BoardRepository;
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

    @Transactional
    public Long save(Member member, SaveBoardRequest saveBoardRequest) {
        Board board = saveBoardRequest.toBoard(member);
        saveBoardRequest.getImageUrls().forEach(
                imageUrl -> {
                    Image image = new Image(board, imageUrl);
                    board.addImage(image);
                }
        );
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
