package com.fit.fit_be.domain.like.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.fit_be.domain.board.fixture.BoardFixture;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.boardcloth.fixture.BoardClothFixture;
import com.fit.fit_be.domain.boardcloth.model.BoardCloth;
import com.fit.fit_be.domain.cloth.fiture.ClothFixture;
import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.repository.ClothRepository;
import com.fit.fit_be.domain.image.fixture.ImageFixture;
import com.fit.fit_be.domain.image.model.Image;
import com.fit.fit_be.domain.like.fixture.LikeFixture;
import com.fit.fit_be.domain.like.model.Likes;
import com.fit.fit_be.domain.like.repository.LikeRepository;
import com.fit.fit_be.domain.member.fixture.MemberFixture;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.domain.member.repository.MemberRepository;
import com.fit.fit_be.global.auth.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ClothRepository clothRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    JavaMailSender javaMailSender;

    String token;

    Member member;

    Cloth cloth;

    @BeforeEach
    void setUp() {

        member = MemberFixture.createMember();
        memberRepository.save(member);

        cloth = ClothFixture.createCloth(member);
        clothRepository.save(cloth);

        token = jwtTokenProvider.createToken(member.getId());
    }

    @Test
    @Transactional
    void 좋아요_저장_API_성공() throws Exception {

        Board board = BoardFixture.createBoard(member);
        Image image = ImageFixture.createImage(board);
        BoardCloth boardCloth = BoardClothFixture.createBoardCloth(board, cloth);
        board.addBoardCloth(boardCloth);
        board.addImage(image);
        boardRepository.save(board);

        mockMvc.perform(MockMvcRequestBuilders.post("/boards/{boardId}/like", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @Transactional
    void 좋아요_삭제_API_성공() throws Exception {

        Board board = BoardFixture.createBoard(member);
        Image image = ImageFixture.createImage(board);
        BoardCloth boardCloth = BoardClothFixture.createBoardCloth(board, cloth);
        board.addBoardCloth(boardCloth);
        board.addImage(image);
        boardRepository.save(board);

        Likes likes = LikeFixture.createLike(board, member);
        likeRepository.save(likes);

        mockMvc.perform(MockMvcRequestBuilders.delete("/boards/{boardId}/like", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

    }
}