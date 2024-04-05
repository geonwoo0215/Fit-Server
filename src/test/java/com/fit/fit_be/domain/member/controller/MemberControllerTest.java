package com.fit.fit_be.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.fit_be.domain.member.dto.request.DeleteMemberRequest;
import com.fit.fit_be.domain.member.dto.request.MemberSignUpRequest;
import com.fit.fit_be.domain.member.fixture.MemberFixture;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.domain.member.repository.MemberRepository;
import com.fit.fit_be.domain.member.service.MemberService;
import com.fit.fit_be.global.auth.jwt.JwtTokenProvider;
import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    JavaMailSender javaMailSender;

    @Test
    @Transactional
    void 사용자_저장_API_성공() throws Exception {

        MemberSignUpRequest memberSignUpRequest = MemberFixture.createMemberSignUpRequest();

        String json = objectMapper.writeValueAsString(memberSignUpRequest);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.startsWith("/members/")))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/members/*"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("member-save",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
                                PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                PayloadDocumentation.fieldWithPath("nickname").type(JsonFieldType.STRING).description("사용자 닉네임")
                        )
                ));
    }

    @Test
    @Transactional
    void 사용자_조회_API_성공() throws Exception {

        Member member = MemberFixture.createMember();
        memberRepository.save(member);
        String token = jwtTokenProvider.createToken(member.getId());

        mockMvc.perform(RestDocumentationRequestBuilders.get("/members/my-profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("member-profile",
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING).description("사용자 프로필 이미지"),
                                PayloadDocumentation.fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                PayloadDocumentation.fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("사용자 닉네임")
                        )
                ));
    }

    @Test
    @Transactional
    void 사용자_삭제_API_성공() throws Exception {

        MemberSignUpRequest memberSignUpRequest = MemberFixture.createMemberSignUpRequest();
        Long id = memberService.singUp(memberSignUpRequest);
        String token = jwtTokenProvider.createToken(id);

        DeleteMemberRequest deleteMemberRequest = MemberFixture.createDeleteMemberRequest();
        String json = objectMapper.writeValueAsString(deleteMemberRequest);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcRestDocumentation.document("member-delete",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호")
                        )
                ));
    }

}