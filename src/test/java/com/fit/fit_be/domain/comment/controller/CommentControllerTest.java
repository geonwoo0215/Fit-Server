package com.fit.fit_be.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.fit_be.domain.board.fixture.BoardFixture;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.cloth.fiture.ClothFixture;
import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.repository.ClothRepository;
import com.fit.fit_be.domain.comment.dto.request.CommentSaveRequest;
import com.fit.fit_be.domain.comment.fixture.CommentFixture;
import com.fit.fit_be.domain.comment.model.Comment;
import com.fit.fit_be.domain.comment.repository.CommentRepository;
import com.fit.fit_be.domain.member.fixture.MemberFixture;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.domain.member.repository.MemberRepository;
import com.fit.fit_be.global.auth.jwt.JwtTokenProvider;
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

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
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
    CommentRepository commentRepository;

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

        member = MemberFixture.createMember();
        memberRepository.save(member);
        token = jwtTokenProvider.createToken(member.getId());

        cloth = ClothFixture.createCloth(member);
        clothRepository.save(cloth);

        board = BoardFixture.createBoard(member);
        boardRepository.save(board);
    }

    @Test
    @Transactional
    void 댓글_저장_API_성공() throws Exception {

        CommentSaveRequest commentSaveRequest = CommentFixture.createCommentSaveRequest();
        String json = objectMapper.writeValueAsString(commentSaveRequest);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/boards/{boardId}/comments", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("comment-save",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("boardId").description("게시글 아이디")
                        ),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글 아이디"),
                                PayloadDocumentation.fieldWithPath("comment").type(JsonFieldType.STRING).description("댓글 내용")
                        )
                ));
    }

    @Test
    @Transactional
    void 댓글_조회_API_성공() throws Exception {

        Comment comment = CommentFixture.createComment(member, board);
        commentRepository.save(comment);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/boards/{boardId}/comments", board.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print()).andDo(MockMvcRestDocumentation.document("comment-save",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("boardId").description("게시글 아이디")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER).description("게시글 아이디"),
                                PayloadDocumentation.fieldWithPath("data.content[].comment").type(JsonFieldType.STRING).description("게시글 아이디"),
                                PayloadDocumentation.fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING).description("게시글 아이디"),
                                PayloadDocumentation.fieldWithPath("data.content[].parentCommentMemberNickname").type(JsonFieldType.STRING).description("게시글 아이디"),
                                PayloadDocumentation.fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("페이지 오프셋"),
                                PayloadDocumentation.fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                PayloadDocumentation.fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER)
                                        .description("한 페이지에 나타내는 원소 수"),
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
}