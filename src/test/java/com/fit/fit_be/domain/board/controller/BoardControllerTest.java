package com.fit.fit_be.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.fit_be.domain.board.dto.request.SaveBoardRequest;
import com.fit.fit_be.domain.board.dto.request.UpdateBoardRequest;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.model.ClothType;
import com.fit.fit_be.domain.cloth.repository.ClothRepository;
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

import java.util.List;
import java.util.Map;

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
    ClothRepository clothRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    String token;

    Member member;

    Cloth cloth;

    @BeforeEach
    void setUp() {
        String loginId = "loginId";
        String password = "password";
        String nickname = "nickname";
        String email = "email";

        String information = "information";
        String size = "M";
        Boolean shoe = false;

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
                .shoe(shoe)
                .build();
        clothRepository.save(cloth);

        token = jwtTokenProvider.createToken(member.getId());
    }

    @Test
    @Transactional
    void 게시글_저장_API_성공() throws Exception {

        String content = "content";
        Long lowestTemperature = -14L;
        Long highestTemperature = -10L;
        boolean open = true;
        List<String> imageUrls = List.of("imageUrl");
        Map<Long, Boolean> clothAppropriate = Map.of(cloth.getId(), true);
        SaveBoardRequest saveBoardRequest = new SaveBoardRequest(content, lowestTemperature, highestTemperature, open, Weather.RAIN, RoadCondition.SLIPPERY, clothAppropriate, imageUrls);

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
    void 게시글_아이디로_게시글_조회_API_성공() throws Exception {

        String content = "content";
        Long lowestTemperature = -14L;
        Long highestTemperature = -10L;
        boolean open = true;

        Board board = new Board(member, content, lowestTemperature, highestTemperature, open, Weather.RAIN, RoadCondition.SLIPPERY);
        boardRepository.save(board);

        mockMvc.perform(MockMvcRequestBuilders.get("/boards/{boardId}", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 게시글_아이디로_게시글_수정_API_성공() throws Exception {

        String content = "content";
        Long lowestTemperature = -14L;
        Long highestTemperature = -10L;
        boolean open = true;

        Board board = Board.builder()
                .member(member)
                .content(content)
                .lowestTemperature(lowestTemperature)
                .highestTemperature(highestTemperature)
                .open(open)
                .weather(Weather.RAIN)
                .roadCondition(RoadCondition.SLIPPERY)
                .build();

        boardRepository.save(board);

        String updateContent = "updateContent";
        UpdateBoardRequest updateBoardRequest = new UpdateBoardRequest(updateContent, null, null, null, null, null);

        String json = objectMapper.writeValueAsString(updateBoardRequest);

        mockMvc.perform(MockMvcRequestBuilders.patch("/boards/{boardId}", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.startsWith("/boards/")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 게시글_아이디로_게시글_삭제_API_성공() throws Exception {

        String content = "content";
        Long lowestTemperature = -14L;
        Long highestTemperature = -10L;
        boolean open = true;

        Board board = new Board(member, content, lowestTemperature, highestTemperature, open, Weather.RAIN, RoadCondition.SLIPPERY);
        boardRepository.save(board);

        mockMvc.perform(MockMvcRequestBuilders.delete("/boards/{boardId}", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }


}