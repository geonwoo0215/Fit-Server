package com.fit.fit_be.domain.board.repository;

import com.fit.fit_be.config.DataLoader;
import com.fit.fit_be.config.TestConfig;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.model.Place;
import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import com.fit.fit_be.domain.like.model.Likes;
import com.fit.fit_be.domain.like.repository.LikeRepository;
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
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Test
    @Rollback(value = false)
    void 더미데이터() {
        String password = "password";
        String email = "email";
        String nickname = "nickname";
        String profileImageUrl = "profileImageUrl";
        Member member = new Member(password, email, nickname, profileImageUrl);
        memberRepository.save(member);
        List<Board> boardList = new ArrayList<>();
        List<Likes> likesList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Board board = Board.builder()
                    .id(i + 1L)
                    .member(member)
                    .content("content")
                    .lowestTemperature(1L)
                    .highestTemperature(5L)
                    .open(true)
                    .weather(Weather.SUNNY)
                    .roadCondition(RoadCondition.NORMAL)
                    .place(Place.OUTING)
                    .build();
            for (int j = 0; j < ((int) (Math.random() * 10) + 1); j++) {
                Likes likes = Likes.of(board, member);
                likesList.add(likes);
                board.addLike(likes);
                board.increaseLikeCount();
            }
            boardList.add(board);
        }
        dataLoader.batchInsertBoard(boardList);
        dataLoader.batchInsertLike(likesList);
    }

    @Test
    void 일정_기간동안_좋아요수_증가_내림차순으로_게시글_조회() {

        String password = "password";
        String email = "email";
        String nickname = "nickname";
        String profileImageUrl = "profileImageUrl";
        Member member = new Member(password, email, nickname, profileImageUrl);
        memberRepository.save(member);

        int page = 0;
        int size = 10;

        Board board1 = Board.builder()
                .member(member)
                .content("content")
                .lowestTemperature(1L)
                .highestTemperature(5L)
                .open(true)
                .weather(Weather.SUNNY)
                .roadCondition(RoadCondition.NORMAL)
                .place(Place.OUTING)
                .build();
        Board board2 = Board.builder()
                .member(member)
                .content("content")
                .lowestTemperature(1L)
                .highestTemperature(5L)
                .open(true)
                .weather(Weather.SUNNY)
                .roadCondition(RoadCondition.NORMAL)
                .place(Place.OUTING)
                .build();
        Board board3 = Board.builder()
                .member(member)
                .content("content")
                .lowestTemperature(1L)
                .highestTemperature(5L)
                .open(true)
                .weather(Weather.SUNNY)
                .roadCondition(RoadCondition.NORMAL)
                .place(Place.OUTING)
                .build();

        boardRepository.save(board1);
        boardRepository.save(board2);
        boardRepository.save(board3);


        IntStream.range(0, 10)
                .mapToObj(i -> {
                    board1.increaseLikeCount();
                    return Likes.of(board1, member);
                }).forEach(likeRepository::save);

        IntStream.range(0, 20)
                .mapToObj(i -> {
                    board2.increaseLikeCount();
                    return Likes.of(board2, member);
                }).forEach(likeRepository::save);

        IntStream.range(0, 30)
                .mapToObj(i -> {
                    board3.increaseLikeCount();
                    return Likes.of(board3, member);
                }).forEach(likeRepository::save);

        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime startDate = LocalDateTime.now().minusHours(1L);
        LocalDateTime endDate = LocalDateTime.now().plusHours(1L);
        Page<Board> boards = boardRepository.findAllByLikeIncrease(pageable, startDate, endDate);

        Assertions.assertThat(boards.getTotalElements()).isEqualTo(3L);
        Assertions.assertThat(boards.getContent().get(0).getLikeCount()).isEqualTo(30);
        Assertions.assertThat(boards.getContent().get(1).getLikeCount()).isEqualTo(20);
        Assertions.assertThat(boards.getContent().get(2).getLikeCount()).isEqualTo(10);

    }
}