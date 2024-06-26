package com.fit.fit_be.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.fit_be.config.DataLoader;
import com.fit.fit_be.domain.board.fixture.BoardFixture;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.model.DateRangeType;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.boardcloth.fixture.BoardClothFixture;
import com.fit.fit_be.domain.boardcloth.repository.BoardClothRepository;
import com.fit.fit_be.domain.cloth.fiture.ClothFixture;
import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.repository.ClothRepository;
import com.fit.fit_be.domain.image.fixture.ImageFixture;
import com.fit.fit_be.domain.image.model.Image;
import com.fit.fit_be.domain.image.repository.ImageRepository;
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
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BoardRankControllerTest {

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

    @Autowired
    DataLoader dataLoader;

    @MockBean
    JavaMailSender javaMailSender;

    @MockBean
    DateTimeProvider dateTimeProvider;

    @SpyBean
    AuditingHandler auditingHandler;

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
        auditingHandler.setDateTimeProvider(dateTimeProvider);
    }

    @Test
    @Transactional
    void 전채공개설정되고_게시글_랭킹_조회_API_성공() throws Exception {

        auditingHandler.setDateTimeProvider(dateTimeProvider);
        BDDMockito.given(dateTimeProvider.getNow()).willReturn(Optional.of(LocalDateTime.now().minusDays(1L)));

        List<Board> boards = BoardFixture.createBoards(member, 10);
        boards.forEach(board -> {
            Image image = ImageFixture.createImage(board);
            Likes likes = LikeFixture.createLike(board, member);
            board.addLike(likes);
            board.increaseLikeCount();
            board.addImage(image);
            board.addBoardCloth(BoardClothFixture.createBoardCloth(board, cloth));
        });
        boardRepository.saveAll(boards);

        Integer page = 0;
        Integer size = 5;
        String type = DateRangeType.DAILY.getType();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));
        params.add("size", String.valueOf(size));
        params.add("type", type);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/boards/ranking")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("board-ranking",
                        RequestDocumentation.queryParameters(
                                RequestDocumentation.parameterWithName("size").description("페이지 크기"),
                                RequestDocumentation.parameterWithName("page").description("페이지 번호"),
                                RequestDocumentation.parameterWithName("type").description("랭킹 타입")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("게시글 아이디"),
                                PayloadDocumentation.fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("게시글 내용"),
                                PayloadDocumentation.fieldWithPath("data.[].lowestTemperature").type(JsonFieldType.NUMBER).description("최저온도"),
                                PayloadDocumentation.fieldWithPath("data.[].highestTemperature").type(JsonFieldType.NUMBER).description("최고온도"),
                                PayloadDocumentation.fieldWithPath("data.[].open").type(JsonFieldType.BOOLEAN).description("게시글 공개여부"),
                                PayloadDocumentation.fieldWithPath("data.[].weather").type(JsonFieldType.STRING).description("날씨"),
                                PayloadDocumentation.fieldWithPath("data.[].roadCondition").type(JsonFieldType.STRING).description("바닥상태"),
                                PayloadDocumentation.fieldWithPath("data.[].place").type(JsonFieldType.STRING).description("장소"),
                                PayloadDocumentation.fieldWithPath("data.[].clothResponses[].id").type(JsonFieldType.NUMBER).description("옷 아이디").optional(),
                                PayloadDocumentation.fieldWithPath("data.[].clothResponses[].type").type(JsonFieldType.STRING).description("옷 타입").optional(),
                                PayloadDocumentation.fieldWithPath("data.[].clothResponses[].information").type(JsonFieldType.STRING).description("옷 정보").optional(),
                                PayloadDocumentation.fieldWithPath("data.[].clothResponses[].size").type(JsonFieldType.STRING).description("옷 사이즈").optional(),
                                PayloadDocumentation.fieldWithPath("data.[].imageUrls").type(JsonFieldType.ARRAY).description("게시글 이미지 URL"),
                                PayloadDocumentation.fieldWithPath("data.[].like").type(JsonFieldType.BOOLEAN).description("게시글 좋아요"),
                                PayloadDocumentation.fieldWithPath("data.[].nickname").type(JsonFieldType.STRING).description("게시글 작성자 닉네임"),
                                PayloadDocumentation.fieldWithPath("data.[].mine").type(JsonFieldType.BOOLEAN).description("게시글 소유여부")
                        )
                ));
    }


}
