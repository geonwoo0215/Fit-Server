package com.fit.fit_be.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.fit_be.domain.board.dto.request.SaveBoardRequest;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.domain.member.repository.MemberRepository;
import com.fit.fit_be.global.auth.jwt.JwtTokenProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    String token;

    @BeforeEach
    void setUp() {
        String loginId = "loginId";
        String password = "password";
        String nickname = "nickname";
        String email = "email";

        Member member = Member.builder()
                .loginId(loginId)
                .password(password)
                .nickname(nickname)
                .email(email)
                .build();
        memberRepository.save(member);
        token = jwtTokenProvider.createToken(member.getId());
    }

    @Test
    @Transactional
    void 게시글_저장_API_성공() throws Exception {

        String content = "content";
        Long lowestTemperature = -14L;
        Long highestTemperature = -10L;
        boolean open = true;
        SaveBoardRequest saveBoardRequest = new SaveBoardRequest(content, lowestTemperature, highestTemperature, open, Weather.RAIN, RoadCondition.SLIPPERY);


        String json = objectMapper.writeValueAsString(saveBoardRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/boards")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.startsWith("/boards/")))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/boards/*"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 게시글_아이디로_비용_조회_API_성공() throws Exception {

        String loginId = "loginId";
        String password = "password";
        String email = "email";
        String nickname = "nickname";
        String content = "content";
        Long lowestTemperature = -14L;
        Long highestTemperature = -10L;
        boolean open = true;

        Member member = new Member(loginId, password, email, nickname);

        Board board = new Board(member, content, lowestTemperature, highestTemperature, open, Weather.RAIN, RoadCondition.SLIPPERY);
        boardRepository.save(board);

        mockMvc.perform(MockMvcRequestBuilders.get("/boards/{boardId}", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

}