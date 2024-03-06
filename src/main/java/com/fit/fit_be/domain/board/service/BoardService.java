package com.fit.fit_be.domain.board.service;

import com.fit.fit_be.domain.board.dto.request.SaveBoardRequest;
import com.fit.fit_be.domain.board.dto.request.SearchBoardRequest;
import com.fit.fit_be.domain.board.dto.request.UpdateBoardRequest;
import com.fit.fit_be.domain.board.dto.response.BoardResponse;
import com.fit.fit_be.domain.board.exception.BoardNotFoundException;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.boardcloth.model.BoardCloth;
import com.fit.fit_be.domain.cloth.exception.ClothNotFoundException;
import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.repository.ClothRepository;
import com.fit.fit_be.domain.image.model.Image;
import com.fit.fit_be.domain.like.repository.LikeRepository;
import com.fit.fit_be.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final ClothRepository clothRepository;
    private final LikeRepository likeRepository;

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

    public BoardResponse findById(Long memberId, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));
        boolean like = likeRepository.existsByBoard_IdAndMember_Id(boardId, memberId);
        BoardResponse boardResponse = board.toBoardResponse(like, memberId);
        return boardResponse;
    }

    public Page<BoardResponse> findAllByCondition(SearchBoardRequest searchBoardRequest, Pageable pageable, Long memberId) {
        Page<Board> boards = boardRepository.findAllByCondition(searchBoardRequest, pageable);
        List<BoardResponse> boardResponseList = boards.stream()
                .map(board -> {
                    boolean like = likeRepository.existsByBoard_IdAndMember_Id(board.getId(), memberId);
                    return board.toBoardResponse(like, memberId);
                })
                .toList();
        PageImpl<BoardResponse> boardResponsePage = new PageImpl<>(boardResponseList, pageable, boards.getTotalElements());
        return boardResponsePage;
    }

    public Page<BoardResponse> findAllByDailyLikeCount(Pageable pageable, Long memberId) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.with(LocalTime.of(0, 0));
        Page<Board> boards = boardRepository.findAllByLikeIncrease(pageable, startDate, endDate);
        List<BoardResponse> boardResponseList = boards.stream()
                .map(board -> {
                    boolean like = likeRepository.existsByBoard_IdAndMember_Id(board.getId(), memberId);
                    return board.toBoardResponse(like, memberId);
                })
                .toList();
        PageImpl<BoardResponse> boardResponsePage = new PageImpl<>(boardResponseList, pageable, boards.getTotalElements());
        return boardResponsePage;
    }

    public Page<BoardResponse> findAllByWeeklyLikeCount(Pageable pageable, Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime startDate = endDate.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        Page<Board> boards = boardRepository.findAllByLikeIncrease(pageable, startDate, endDate);
        List<BoardResponse> boardResponseList = boards.stream()
                .map(board -> {
                    boolean like = likeRepository.existsByBoard_IdAndMember_Id(board.getId(), memberId);
                    return board.toBoardResponse(like, memberId);
                })
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
