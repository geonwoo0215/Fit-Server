package com.fit.fit_be.domain.cloth.repository;

import com.fit.fit_be.config.TestConfig;
import com.fit.fit_be.domain.board.repository.BoardRepository;
import com.fit.fit_be.domain.cloth.fiture.ClothFixture;
import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.model.ClothType;
import com.fit.fit_be.domain.member.fixture.MemberFixture;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.domain.member.repository.MemberRepository;
import com.fit.fit_be.global.config.JpaConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@DataJpaTest
@Import({JpaConfig.class, TestConfig.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClothCustomRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ClothRepository clothRepository;

    @Test
    void 옷_타입으로_조회() {

        Member member = MemberFixture.createMember();
        memberRepository.save(member);

        List<Cloth> cloths = ClothFixture.createCloths(member, 10);
        clothRepository.saveAll(cloths);

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Page<Cloth> clothPage = clothRepository.findAllByType(pageable, member.getId(), ClothType.TOP);

        Assertions.assertThat(clothPage.getTotalElements()).isEqualTo(cloths.size());
        clothPage.getContent().stream().forEach(cloth1 -> Assertions.assertThat(cloth1.getType()).isEqualTo(ClothType.TOP));

    }
}
