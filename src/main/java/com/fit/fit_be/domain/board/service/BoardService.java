package com.fit.fit_be.domain.board.service;

import com.fit.fit_be.domain.board.dto.request.SaveBoardRequest;
import com.fit.fit_be.domain.board.dto.request.SearchBoardRequest;
import com.fit.fit_be.domain.board.dto.request.UpdateBoardRequest;
import com.fit.fit_be.domain.board.dto.response.BoardResponse;
import com.fit.fit_be.domain.board.exception.BoardNotFoundException;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.model.DateRangeType;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.boardcloth.model.BoardCloth;
import com.fit.fit_be.domain.cloth.exception.ClothNotFoundException;
import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.repository.ClothRepository;
import com.fit.fit_be.domain.image.model.Image;
import com.fit.fit_be.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final ClothRepository clothRepository;

    @Transactional
    public Long save(Member member, SaveBoardRequest saveBoardRequest) {

        Board board = saveBoardRequest.toBoard(member);
        addImage(saveBoardRequest, board);
        addBoardCloth(saveBoardRequest, board);
        Board saveBoard = boardRepository.save(board);
        return saveBoard.getId();
    }

    private void addBoardCloth(SaveBoardRequest saveBoardRequest, Board board) {
        saveBoardRequest.getClothAppropriates().forEach((clothId, isAppropriate) -> {
            Cloth cloth = clothRepository.findById(clothId)
                    .orElseThrow(() -> new ClothNotFoundException(clothId));
            BoardCloth boardCloth = BoardCloth.of(board, cloth, isAppropriate);
            board.addBoardCloth(boardCloth);
        });
    }

    private void addImage(SaveBoardRequest saveBoardRequest, Board board) {
        saveBoardRequest.getImageUrls().forEach(
                imageUrl -> {
                    Image image = Image.of(board, imageUrl);
                    board.addImage(image);
                }
        );
    }

    public BoardResponse findById(Long memberId, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));
        BoardResponse boardResponse = board.toBoardResponse(memberId);
        return boardResponse;
    }

    public Page<BoardResponse> findAllByCondition(SearchBoardRequest searchBoardRequest, Pageable pageable, Long memberId) {
        Page<Board> boards = boardRepository.findAllByCondition(searchBoardRequest, pageable);
        List<BoardResponse> boardResponseList = boards.stream()
                .map(board -> board.toBoardResponse(memberId))
                .toList();
        PageImpl<BoardResponse> boardResponsePage = new PageImpl<>(boardResponseList, pageable, boards.getTotalElements());
        return boardResponsePage;
    }

    public Page<BoardResponse> findAllByLikeIncrease(Pageable pageable, Long memberId, String type) {
        DateRangeType dateRange = DateRangeType.of(type);

        Page<Board> boards = boardRepository.findAllByLikeIncrease(pageable, dateRange.getStartDate(), dateRange.getEndDate());
        List<BoardResponse> boardResponseList = boards.stream()
                .map(board -> board.toBoardResponse(memberId))
                .toList();
        PageImpl<BoardResponse> boardResponsePage = new PageImpl<>(boardResponseList, pageable, boards.getTotalElements());
        return boardResponsePage;
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
