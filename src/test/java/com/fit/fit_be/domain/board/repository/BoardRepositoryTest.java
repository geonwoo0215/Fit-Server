package com.fit.fit_be.domain.board.repository;

import com.fit.fit_be.config.DataLoader;
import com.fit.fit_be.config.TestConfig;
import com.fit.fit_be.domain.board.fixture.BoardFixture;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.model.DateRangeType;
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
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@DataJpaTest
@Import({TestConfig.class, DataLoader.class, JpaConfig.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    DataLoader dataLoader;

    @MockBean
    DateTimeProvider dateTimeProvider;

    @SpyBean
    AuditingHandler auditingHandler;

    @Test
    @Rollback(value = false)
    void 더미데이터() {
        Member member = MemberFixture.createMember();
        memberRepository.save(member);

        List<Board> boardList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Board board = BoardFixture.createBoard(member);
            boardList.add(board);
        }
        dataLoader.batchInsertBoard(boardList);
    }

    @Test
    @Rollback(value = false)
    void 더미데이터2() {


        Member member = MemberFixture.createMember();
        memberRepository.save(member);
        List<Likes> likesList = new ArrayList<>();
        for (int i = 100000; i < 130000; i++) {
            Board board = BoardFixture.createBoard(member, (long) (i + 1));
            for (int j = 0; j < ((int) (Math.random() * 10) + 1); j++) {
                Likes likes = LikeFixture.createLike(board, member);
                likesList.add(likes);
            }
        }
        dataLoader.batchInsertLike(likesList);
    }

    @Test
    @Rollback(value = false)
    void 일정_기간동안_좋아요수_증가_내림차순으로_게시글_조회() {

        Member member = MemberFixture.createMember();
        memberRepository.save(member);

        auditingHandler.setDateTimeProvider(dateTimeProvider);
        BDDMockito.given(dateTimeProvider.getNow()).willReturn(Optional.of(LocalDateTime.now().minusDays(5L)));

        int page = 0;
        int size = 20;

        List<Board> boards = BoardFixture.createBoards(member, 30);
        boards.forEach(board -> {
            IntStream.range(0, boards.indexOf(board))
                    .forEach(i -> {
                        Likes likes = LikeFixture.createLike(board, member);
                        board.addLike(likes);
                        board.increaseLikeCount();
                    });
        });

        boardRepository.saveAll(boards);

        Pageable pageable = PageRequest.of(page, size);
        DateRangeType dateRangeType = DateRangeType.WEEKLY;
        List<Board> boardList = boardRepository.findAllByLikeIncrease(pageable, dateRangeType.getDates());
        Assertions.assertThat(boardList.size()).isEqualTo(20);
        IntStream.range(0, boardList.size() - 1)
                .forEach(i -> Assertions.assertThat(boardList.get(i).getLikes().size()).isGreaterThan(boardList.get(i + 1).getLikes().size()));
    }
}