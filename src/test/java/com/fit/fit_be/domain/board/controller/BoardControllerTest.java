package com.fit.fit_be.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.fit_be.domain.board.dto.request.SaveBoardRequest;
import com.fit.fit_be.domain.board.dto.request.UpdateBoardRequest;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.model.Place;
import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.boardcloth.model.BoardCloth;
import com.fit.fit_be.domain.boardcloth.repository.BoardClothRepository;
import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.model.ClothType;
import com.fit.fit_be.domain.cloth.repository.ClothRepository;
import com.fit.fit_be.domain.image.model.Image;
import com.fit.fit_be.domain.image.repository.ImageRepository;
import com.fit.fit_be.domain.like.model.Likes;
import com.fit.fit_be.domain.like.repository.LikeRepository;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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
    LikeRepository likeRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ClothRepository clothRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    BoardClothRepository boardClothRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    JavaMailSender javaMailSender;

    String token;

    Member member;

    Cloth cloth;

    @BeforeEach
    void setUp() {
        String password = "password";
        String nickname = "nickname";
        String email = "email";

        String information = "information";
        String size = "M";

        member = Member.builder()
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

        Board board = new Board(member, content, lowestTemperature, highestTemperature, open, Weather.RAIN, RoadCondition.SLIPPERY, Place.OUTING);
        Image image = new Image(board, "imageUrl");
        BoardCloth boardCloth = new BoardCloth(board, cloth, true);
        board.addBoardCloth(boardCloth);
        board.addImage(image);
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
    void 전채공개설정된_게시글들_조회_API_성공() throws Exception {

        String content = "content";
        Long lowestTemperature = -14L;
        Long highestTemperature = -10L;
        boolean open = true;

        Board board = new Board(member, content, lowestTemperature, highestTemperature, open, Weather.RAIN, RoadCondition.SLIPPERY, Place.OUTING);
        Image image = new Image(board, "imageUrl");
        BoardCloth boardCloth = new BoardCloth(board, cloth, true);
        board.addBoardCloth(boardCloth);
        board.addImage(image);
        boardRepository.save(board);

        Integer page = 0;

        Integer size = 5;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));
        params.add("size", String.valueOf(size));

        mockMvc.perform(MockMvcRequestBuilders.get("/boards", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 전채공개설정되고_1주일간좋아요증가량이높은순서로_게시글들_조회_API_성공() throws Exception {

        String content = "content";
        Long lowestTemperature = -14L;
        Long highestTemperature = -10L;
        boolean open = true;

        Board board1 = new Board(member, content, lowestTemperature, highestTemperature, open, Weather.RAIN, RoadCondition.SLIPPERY, Place.OUTING);
        Image image = new Image(board1, "imageUrl");
        BoardCloth boardCloth = new BoardCloth(board1, cloth, true);
        board1.addBoardCloth(boardCloth);
        board1.addImage(image);

        Board board2 = new Board(member, content, lowestTemperature, highestTemperature, open, Weather.RAIN, RoadCondition.SLIPPERY, Place.OUTING);
        boardRepository.save(board1);
        boardRepository.save(board2);

        IntStream.range(1, 20)
                .mapToObj(i -> new Likes(board1, member))
                .forEach(likeRepository::save);

        IntStream.range(1, 10)
                .mapToObj(i -> new Likes(board2, member))
                .forEach(likeRepository::save);


        Integer page = 0;

        Integer size = 5;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));
        params.add("size", String.valueOf(size));

        mockMvc.perform(MockMvcRequestBuilders.get("/boards/weekly-ranking")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .params(params)
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

        Board board = new Board(member, content, lowestTemperature, highestTemperature, open, Weather.RAIN, RoadCondition.SLIPPERY, Place.OUTING);
        boardRepository.save(board);

        mockMvc.perform(MockMvcRequestBuilders.delete("/boards/{boardId}", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }


}