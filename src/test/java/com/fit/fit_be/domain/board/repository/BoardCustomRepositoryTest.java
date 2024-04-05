package com.fit.fit_be.domain.board.repository;

import com.fit.fit_be.config.TestConfig;
import com.fit.fit_be.domain.board.dto.request.SearchBoardRequest;
import com.fit.fit_be.domain.board.fixture.BoardFixture;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.cloth.repository.ClothRepository;
import com.fit.fit_be.domain.like.fixture.LikeFixture;
import com.fit.fit_be.domain.like.model.Likes;
import com.fit.fit_be.domain.like.repository.LikeRepository;
import com.fit.fit_be.domain.member.fixture.MemberFixture;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.domain.member.repository.MemberRepository;
import com.fit.fit_be.global.config.JpaConfig;
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
import java.util.stream.IntStream;

@DataJpaTest
@Import({JpaConfig.class, TestConfig.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardCustomRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ClothRepository clothRepository;

    @Autowired
    LikeRepository likeRepository;

    @Test
    void 검색조건으로_게시글_조회() {

        Member member = MemberFixture.createMember();
        memberRepository.save(member);

        int page = 0;
        int size = 10;

        List<Board> boards = BoardFixture.createBoards(member, 10);
        boards.forEach(board -> {
            IntStream.range(0, boards.indexOf(board))
                    .forEach(i -> {
                        Likes likes = LikeFixture.createLike(board, member);
                        board.addLike(likes);
                        board.increaseLikeCount();
                    });
        });

        boardRepository.saveAll(boards);

        SearchBoardRequest searchBoardRequest = BoardFixture.createSearchBoardRequest();
        Pageable pageable = PageRequest.of(page, size);

        Page<Board> boardPage = boardRepository.findAllByCondition(searchBoardRequest, pageable);

        Assertions.assertThat(boardPage.getTotalElements()).isEqualTo(10L);

    }
}