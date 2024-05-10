package com.fit.fit_be.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.fit_be.config.DataLoader;
import com.fit.fit_be.domain.board.dto.request.SaveBoardRequest;
import com.fit.fit_be.domain.board.dto.request.UpdateBoardRequest;
import com.fit.fit_be.domain.board.fixture.BoardFixture;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.model.Place;
import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.boardcloth.fixture.BoardClothFixture;
import com.fit.fit_be.domain.boardcloth.model.BoardCloth;
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
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
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

    @Autowired
    DataLoader dataLoader;

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
    void 게시글_저장_API_성공() throws Exception {

        List<String> imageUrls = List.of("imageUrl");
        Map<Long, Boolean> clothAppropriate = Map.of(cloth.getId(), true);
        SaveBoardRequest saveBoardRequest = BoardFixture.createSaveBoardRequest(imageUrls, clothAppropriate);

        String json = objectMapper.writeValueAsString(saveBoardRequest);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/boards")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.startsWith("/boards/")))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/boards/*"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("board-save",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                PayloadDocumentation.fieldWithPath("lowestTemperature").type(JsonFieldType.NUMBER).description("최저기온"),
                                PayloadDocumentation.fieldWithPath("highestTemperature").type(JsonFieldType.NUMBER).description("최고기온"),
                                PayloadDocumentation.fieldWithPath("open").type(JsonFieldType.BOOLEAN).description("공개여부"),
                                PayloadDocumentation.fieldWithPath("weather").type(JsonFieldType.STRING).description("날씨"),
                                PayloadDocumentation.fieldWithPath("roadCondition").type(JsonFieldType.STRING).description("바닥 상태"),
                                PayloadDocumentation.fieldWithPath("place").type(JsonFieldType.STRING).description("장소"),
                                PayloadDocumentation.subsectionWithPath("clothAppropriates").type(JsonFieldType.OBJECT).description("옷 적절 여부"),
                                PayloadDocumentation.fieldWithPath("imageUrls").type(JsonFieldType.ARRAY).description("이지미 url")
                        )
                ));
    }

    @Test
    @Transactional
    void 게시글_아이디로_게시글_조회_API_성공() throws Exception {

        Board board = BoardFixture.createBoard(member);
        Image image = ImageFixture.createImage(board);
        BoardCloth boardCloth = BoardClothFixture.createBoardCloth(board, cloth);
        board.addBoardCloth(boardCloth);
        board.addImage(image);
        boardRepository.save(board);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/boards/{boardId}", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("board-findById",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("boardId").description("게시글 아이디")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 아이디"),
                                PayloadDocumentation.fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 내용"),
                                PayloadDocumentation.fieldWithPath("data.lowestTemperature").type(JsonFieldType.NUMBER).description("최저온도"),
                                PayloadDocumentation.fieldWithPath("data.highestTemperature").type(JsonFieldType.NUMBER).description("최고온도"),
                                PayloadDocumentation.fieldWithPath("data.open").type(JsonFieldType.BOOLEAN).description("게시글 공개여부"),
                                PayloadDocumentation.fieldWithPath("data.weather").type(JsonFieldType.STRING).description("날씨"),
                                PayloadDocumentation.fieldWithPath("data.roadCondition").type(JsonFieldType.STRING).description("바닥상태"),
                                PayloadDocumentation.fieldWithPath("data.place").type(JsonFieldType.STRING).description("장소"),
                                PayloadDocumentation.fieldWithPath("data.clothResponses[].id").type(JsonFieldType.NUMBER).description("옷 아이디"),
                                PayloadDocumentation.fieldWithPath("data.clothResponses[].type").type(JsonFieldType.STRING).description("옷 타입"),
                                PayloadDocumentation.fieldWithPath("data.clothResponses[].information").type(JsonFieldType.STRING).description("옷 정보"),
                                PayloadDocumentation.fieldWithPath("data.clothResponses[].size").type(JsonFieldType.STRING).description("옷 사이즈"),
                                PayloadDocumentation.fieldWithPath("data.imageUrls").type(JsonFieldType.ARRAY).description("게시글 이미지 URL"),
                                PayloadDocumentation.fieldWithPath("data.like").type(JsonFieldType.BOOLEAN).description("게시글 좋아요"),
                                PayloadDocumentation.fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("게시글 작성자 닉네임"),
                                PayloadDocumentation.fieldWithPath("data.mine").type(JsonFieldType.BOOLEAN).description("게시글 소유여부")
                        )
                ));
    }

    @Test
    @Transactional
    void 전채공개설정된_게시글들_조회_API_성공() throws Exception {

        Board board = BoardFixture.createBoard(member);
        Image image = ImageFixture.createImage(board);
        BoardCloth boardCloth = BoardClothFixture.createBoardCloth(board, cloth);
        board.addBoardCloth(boardCloth);
        board.addImage(image);
        boardRepository.save(board);

        Integer page = 0;
        Integer size = 5;
        Long lowestTemperature = -20L;
        Long highestTemperature = 10L;
        String weather = Weather.SUNNY.getWeather();
        String roadCondition = RoadCondition.NORMAL.getRoadCondition();
        String place = Place.OUTING.getPlace();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));
        params.add("size", String.valueOf(size));
        params.add("lowestTemperature", String.valueOf(lowestTemperature));
        params.add("highestTemperature", String.valueOf(highestTemperature));
        params.add("weather", weather);
        params.add("roadCondition", roadCondition);
        params.add("place", place);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/boards")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("board-findAll",
                        RequestDocumentation.queryParameters(
                                RequestDocumentation.parameterWithName("size").description("페이지 크기"),
                                RequestDocumentation.parameterWithName("page").description("페이지 번호"),
                                RequestDocumentation.parameterWithName("lowestTemperature").description("최저 기온"),
                                RequestDocumentation.parameterWithName("highestTemperature").description("최저 기온"),
                                RequestDocumentation.parameterWithName("weather").description("날씨"),
                                RequestDocumentation.parameterWithName("roadCondition").description("바닥 상태"),
                                RequestDocumentation.parameterWithName("place").description("장소")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER).description("게시글 아이디"),
                                PayloadDocumentation.fieldWithPath("data.content[].content").type(JsonFieldType.STRING).description("게시글 내용"),
                                PayloadDocumentation.fieldWithPath("data.content[].lowestTemperature").type(JsonFieldType.NUMBER).description("최저온도"),
                                PayloadDocumentation.fieldWithPath("data.content[].highestTemperature").type(JsonFieldType.NUMBER).description("최고온도"),
                                PayloadDocumentation.fieldWithPath("data.content[].open").type(JsonFieldType.BOOLEAN).description("게시글 공개여부"),
                                PayloadDocumentation.fieldWithPath("data.content[].weather").type(JsonFieldType.STRING).description("날씨"),
                                PayloadDocumentation.fieldWithPath("data.content[].roadCondition").type(JsonFieldType.STRING).description("바닥상태"),
                                PayloadDocumentation.fieldWithPath("data.content[].place").type(JsonFieldType.STRING).description("장소"),
                                PayloadDocumentation.fieldWithPath("data.content[].clothResponses[]").optional().type(JsonFieldType.ARRAY).description("게시글에 첨부된 옷 정보 배열"),
                                PayloadDocumentation.fieldWithPath("data.content[].clothResponses[].id").type(JsonFieldType.NUMBER).description("옷 아이디").optional(),
                                PayloadDocumentation.fieldWithPath("data.content[].clothResponses[].type").type(JsonFieldType.STRING).description("옷 타입").optional(),
                                PayloadDocumentation.fieldWithPath("data.content[].clothResponses[].information").type(JsonFieldType.STRING).description("옷 정보").optional(),
                                PayloadDocumentation.fieldWithPath("data.content[].clothResponses[].size").type(JsonFieldType.STRING).description("옷 사이즈").optional(),
                                PayloadDocumentation.fieldWithPath("data.content[].imageUrls").type(JsonFieldType.ARRAY).description("게시글 이미지 URL"),
                                PayloadDocumentation.fieldWithPath("data.content[].like").type(JsonFieldType.BOOLEAN).description("게시글 좋아요"),
                                PayloadDocumentation.fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING).description("게시글 작성자 닉네임"),
                                PayloadDocumentation.fieldWithPath("data.content[].mine").type(JsonFieldType.BOOLEAN).description("게시글 소유여부"),
                                PayloadDocumentation.fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("페이지 오프셋"),
                                PayloadDocumentation.fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                PayloadDocumentation.fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("한 페이지에 나타내는 원소 수"),
                                PayloadDocumentation.fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이지 정보 포함 여부"),
                                PayloadDocumentation.fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이지 정보 비포함 여부"),
                                PayloadDocumentation.fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                PayloadDocumentation.fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
                                PayloadDocumentation.fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                PayloadDocumentation.fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("빈 페이지 여부"),
                                PayloadDocumentation.fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("페이지 정렬 여부"),
                                PayloadDocumentation.fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("페이지 비정렬 여부"),
                                PayloadDocumentation.fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("빈 페이지 여부"),
                                PayloadDocumentation.fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("페이지 정렬 여부"),
                                PayloadDocumentation.fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("페이지 비정렬 여부"),
                                PayloadDocumentation.fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 번째 페이지 여부"),
                                PayloadDocumentation.fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("페이지 원소 개수"),
                                PayloadDocumentation.fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("빈 페이지 여부"),
                                PayloadDocumentation.fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 개수"),
                                PayloadDocumentation.fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 개수")
                        )
                ));
    }

    @Test
    @Transactional
    void 게시글_아이디로_게시글_수정_API_성공() throws Exception {

        Board board = BoardFixture.createBoard(member);
        boardRepository.save(board);

        UpdateBoardRequest updateBoardRequest = BoardFixture.createUpdateBoardRequest();
        String json = objectMapper.writeValueAsString(updateBoardRequest);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/boards/{boardId}", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.startsWith("/boards/")))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("board-update",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                PayloadDocumentation.fieldWithPath("lowestTemperature").type(JsonFieldType.NUMBER).description("최저기온"),
                                PayloadDocumentation.fieldWithPath("highestTemperature").type(JsonFieldType.NUMBER).description("최고기온"),
                                PayloadDocumentation.fieldWithPath("open").type(JsonFieldType.BOOLEAN).description("공개여부"),
                                PayloadDocumentation.fieldWithPath("weather").type(JsonFieldType.STRING).description("날씨"),
                                PayloadDocumentation.fieldWithPath("roadCondition").type(JsonFieldType.STRING).description("바닥 상태"),
                                PayloadDocumentation.fieldWithPath("place").type(JsonFieldType.STRING).description("장소")
                        )
                ));
    }

    @Test
    @Transactional
    void 게시글_아이디로_게시글_삭제_API_성공() throws Exception {

        Board board = BoardFixture.createBoard(member);
        boardRepository.save(board);

        Image image = ImageFixture.createImage(board);
        imageRepository.save(image);

        Likes likes = LikeFixture.createLike(board, member);
        likeRepository.save(likes);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/boards/{boardId}", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("board-delete",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("boardId").description("게시글 아이디")
                        )
                ));
    }


}