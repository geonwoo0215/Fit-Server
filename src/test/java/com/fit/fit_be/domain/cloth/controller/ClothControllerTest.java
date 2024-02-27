package com.fit.fit_be.domain.cloth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.fit_be.domain.cloth.dto.request.SaveClothRequest;
import com.fit.fit_be.domain.cloth.dto.request.UpdateClothRequest;
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
        String loginId = "loginId";
        String password = "password";
        String nickname = "nickname";
        String email = "email";

        member = Member.builder()
                .password(password)
                .nickname(nickname)
                .email(email)
                .build();
        memberRepository.save(member);
        token = jwtTokenProvider.createToken(member.getId());
    }

    @Test
    @Transactional
    void 옷_저장_API_성공() throws Exception {


        String information = "information";
        String size = "M";

        SaveClothRequest saveClothRequest = new SaveClothRequest(ClothType.TOP, information, size);

        String json = objectMapper.writeValueAsString(saveClothRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/cloths")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.startsWith("/cloths/")))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("/cloths/*"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 옷_아이디로_옷_조회_API_성공() throws Exception {

        String information = "information";
        String size = "M";
        Boolean shoe = false;

        Cloth cloth = Cloth.builder()
                .member(member)
                .information(information)
                .size(size)
                .type(ClothType.TOP)
                .build();

        clothRepository.save(cloth);

        mockMvc.perform(MockMvcRequestBuilders.get("/cloths/{clothId}", cloth.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 옷_아이디로_옷_수정_API_성공() throws Exception {

        String information = "information";
        String size = "M";

        Cloth cloth = Cloth.builder()
                .member(member)
                .information(information)
                .size(size)
                .type(ClothType.TOP)
                .build();

        clothRepository.save(cloth);

        String updateSize = "L";
        UpdateClothRequest updateClothRequest = new UpdateClothRequest(null, null, updateSize);

        String json = objectMapper.writeValueAsString(updateClothRequest);

        mockMvc.perform(MockMvcRequestBuilders.patch("/cloths/{clothId}", cloth.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.startsWith("/cloths/")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 옷_아이디로_옷_삭제_API_성공() throws Exception {

        String information = "information";
        String size = "M";

        Cloth cloth = Cloth.builder()
                .member(member)
                .information(information)
                .size(size)
                .type(ClothType.TOP)
                .build();

        clothRepository.save(cloth);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cloths/{clothId}", cloth.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }

}