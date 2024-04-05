package com.fit.fit_be.domain.cloth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.fit_be.domain.cloth.dto.request.SaveClothRequest;
import com.fit.fit_be.domain.cloth.dto.request.UpdateClothRequest;
import com.fit.fit_be.domain.cloth.fiture.ClothFixture;
import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.model.ClothType;
import com.fit.fit_be.domain.cloth.repository.ClothRepository;
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

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ClothControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ClothRepository clothRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    JavaMailSender javaMailSender;

    String token;

    Member member;

    @BeforeEach
    void setUp() {
        member = MemberFixture.createMember();
        memberRepository.save(member);
        token = jwtTokenProvider.createToken(member.getId());
    }

    @Test
    @Transactional
    void 옷_저장_API_성공() throws Exception {

        SaveClothRequest saveClothRequest = ClothFixture.createSaveClothRequest();
        String json = objectMapper.writeValueAsString(saveClothRequest);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/cloths")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.startsWith("/cloths/")))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/cloths/*"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("cloth-save",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("type").type(JsonFieldType.STRING).description("옷 타입"),
                                PayloadDocumentation.fieldWithPath("information").type(JsonFieldType.STRING).description("옷 설명"),
                                PayloadDocumentation.fieldWithPath("size").type(JsonFieldType.STRING).description("옷 사이즈")
                        )
                ));
    }

    @Test
    @Transactional
    void 옷_아이디로_옷_조회_API_성공() throws Exception {

        Cloth cloth = ClothFixture.createCloth(member);
        clothRepository.save(cloth);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/cloths/{clothId}", cloth.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("cloth-findById",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("clothId").description("옷 아이디")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("옷 아이디"),
                                PayloadDocumentation.fieldWithPath("data.type").type(JsonFieldType.STRING).description("옷 타입"),
                                PayloadDocumentation.fieldWithPath("data.information").type(JsonFieldType.STRING).description("옷 정보"),
                                PayloadDocumentation.fieldWithPath("data.size").type(JsonFieldType.STRING).description("옷 사이즈")
                        )
                ));
    }

    @Test
    @Transactional
    void 옷_타입_옷_조회_API_성공() throws Exception {

        Cloth cloth = ClothFixture.createCloth(member);
        clothRepository.save(cloth);

        String type = ClothType.TOP.getClothType();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", type);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/cloths")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("cloth-type",
                        RequestDocumentation.queryParameters(
                                RequestDocumentation.parameterWithName("type").description("타입")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER).description("옷 아이디"),
                                PayloadDocumentation.fieldWithPath("data.content[].type").type(JsonFieldType.STRING).description("옷 타입"),
                                PayloadDocumentation.fieldWithPath("data.content[].information").type(JsonFieldType.STRING).description("옷 정보"),
                                PayloadDocumentation.fieldWithPath("data.content[].size").type(JsonFieldType.STRING).description("옷 사이즈"),
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
    void 옷_아이디로_옷_수정_API_성공() throws Exception {

        Cloth cloth = ClothFixture.createCloth(member);
        clothRepository.save(cloth);

        UpdateClothRequest updateClothRequest = ClothFixture.createUpdateClothRequest();
        String json = objectMapper.writeValueAsString(updateClothRequest);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/cloths/{clothId}", cloth.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.startsWith("/cloths/")))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("cloth-update",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("type").type(JsonFieldType.STRING).description("옷 타입"),
                                PayloadDocumentation.fieldWithPath("information").type(JsonFieldType.STRING).description("옷 정보"),
                                PayloadDocumentation.fieldWithPath("size").type(JsonFieldType.STRING).description("옷 사이즈")
                        )
                ));
    }

    @Test
    @Transactional
    void 옷_아이디로_옷_삭제_API_성공() throws Exception {

        Cloth cloth = ClothFixture.createCloth(member);
        clothRepository.save(cloth);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/cloths/{clothId}", cloth.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("cloth-delete",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("clothId").description("옷 아이디")
                        )
                ));
    }

}