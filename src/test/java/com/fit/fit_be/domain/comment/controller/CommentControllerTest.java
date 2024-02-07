package com.fit.fit_be.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.model.ClothType;
import com.fit.fit_be.domain.cloth.repository.ClothRepository;
import com.fit.fit_be.domain.comment.dto.request.CommentSaveRequest;
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
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    Member member;

    Board board;
    Cloth cloth;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ClothRepository clothRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        String loginId = "loginId";
        String password = "password";
        String nickname = "nickname";
        String email = "email";

        String information = "information";
        String size = "M";

        member = Member.builder()
                .loginId(loginId)
                .password(password)
                .nickname(nickname)
                .email(email)
                .build();
        memberRepository.save(member);

        cloth = Cloth.builder()
                .member(member)
                .type(ClothType.TOP)
                .information(information)
                .size(size)
                .build();
        clothRepository.save(cloth);

        token = jwtTokenProvider.createToken(member.getId());

        board = Board.builder()
                .member(member)
                .content("content")
                .lowestTemperature(1L)
                .highestTemperature(8L)
                .open(false)
                .weather(Weather.RAIN)
                .roadCondition(RoadCondition.SLIPPERY)
                .build();

        boardRepository.save(board);
    }

    @Test
    @Transactional
    void 댓글_저장_API_성공() throws Exception {

        CommentSaveRequest commentSaveRequest = new CommentSaveRequest(0L, "comment");
        String json = objectMapper.writeValueAsString(commentSaveRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/boards/{boardId}/comments", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 댓글_조회_API_성공() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.get("/boards/{boardId}/comments", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }
}