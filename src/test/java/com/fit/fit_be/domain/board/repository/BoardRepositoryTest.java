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
@Import({TestConfig.class, DataLoader.class})
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

//    @Test
//    @Rollback(value = false)
//    void 더미데이터() {
//        String password = "password";
//        String email = "email";
//        String nickname = "nickname";
//        String profileImageUrl = "profileImageUrl";
//        Member member = new Member(password, email, nickname, profileImageUrl);
//        memberRepository.save(member);
//        List<Board> boardList = new ArrayList<>();
//        List<Likes> likesList = new ArrayList<>();
//        for (int i = 0; i < 10000; i++) {
//            Board board = Board.builder()
//                    .id(i + 1L)
//                    .member(member)
//                    .content("content")
//                    .lowestTemperature(1L)
//                    .highestTemperature(5L)
//                    .open(true)
//                    .weather(Weather.SUNNY)
//                    .roadCondition(RoadCondition.NORMAL)
//                    .place(Place.OUTING)
//                    .build();
//            for (int j = 0; j < ((int) (Math.random() * 10) + 1); j++) {
//                Likes likes = Likes.of(board, member);
//                likesList.add(likes);
//                board.addLike(likes);
//                board.increaseLikeCount();
//            }
//            boardList.add(board);
//        }
//        dataLoader.batchInsertBoard(boardList);
//        dataLoader.batchInsertLike(likesList);
//    }

    @Test
    void 일정_기간동안_좋아요수_증가_내림차순으로_게시글_조회() {

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

        Pageable pageable = PageRequest.of(page, size);
        DateRangeType dateRangeType = DateRangeType.DAILY;
        Page<Board> boardPage = boardRepository.findAllByLikeIncrease(pageable, dateRangeType.getStartDate(), dateRangeType.getEndDate());

        IntStream.range(0, boardPage.getContent().size() - 1)
                .forEach(i -> Assertions.assertThat(boardPage.getContent().get(i).getLikes().size()).isGreaterThan(boardPage.getContent().get(i + 1).getLikes().size()));
    }
}