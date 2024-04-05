package com.fit.fit_be.domain.comment.repository;

import com.fit.fit_be.config.DataLoader;
import com.fit.fit_be.config.TestConfig;
import com.fit.fit_be.domain.board.fixture.BoardFixture;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.comment.dto.response.CommentResponse;
import com.fit.fit_be.domain.comment.fixture.CommentFixture;
import com.fit.fit_be.domain.comment.model.Comment;
import com.fit.fit_be.domain.member.fixture.MemberFixture;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@DataJpaTest
@Import({TestConfig.class, DataLoader.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Test
    void 게시글_아이디로_댓글_최대그룹번호_조회() {

        Member member = MemberFixture.createMember();
        memberRepository.save(member);

        Board board = BoardFixture.createBoard(member);
        boardRepository.save(board);

        List<Comment> comments = CommentFixture.createComments(member, board, 10);
        commentRepository.saveAll(comments);

        Long maxGroupNo = commentRepository.findMaxGroupNoByBoardId(board.getId());

        Assertions.assertThat(maxGroupNo).isEqualTo(comments.size() - 1);
    }

    @Test
    void 게시글_아이디로_댓글_조회() {

        Member member = MemberFixture.createMember();
        memberRepository.save(member);

        Board board = BoardFixture.createBoard(member);
        boardRepository.save(board);

        List<Comment> comments = CommentFixture.createComments(member, board, 10);
        commentRepository.saveAll(comments);

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentResponse> commentResponsePage = commentRepository.findAllByBoardId(board.getId(), pageable);

        Assertions.assertThat(commentResponsePage.getTotalElements()).isEqualTo(10);
    }

}