package com.fit.fit_be.domain.like.service;

import com.fit.fit_be.domain.board.exception.BoardNotFoundException;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.like.model.Likes;
import com.fit.fit_be.domain.like.repository.LikeRepository;
import com.fit.fit_be.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void save(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));
        board.increaseLikeCount();
        Likes likes = Likes.of(board, member);
        likeRepository.save(likes);
    }

    @Transactional
    public void delete(Long boardId, Member member) {
        likeRepository.deleteByBoard_IdAndMember(boardId, member);
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
        board.decreaseLikeCount();
    }

}
