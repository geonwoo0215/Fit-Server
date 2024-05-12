package com.fit.fit_be.domain.weather.repository;

import com.fit.fit_be.config.DataLoader;
import com.fit.fit_be.config.TestConfig;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({TestConfig.class, DataLoader.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WeatherAreaRepositoryTest {

    @Autowired
    WeatherAreaRepository weatherAreaRepository;

//    @Test
//    void x좌표와_y좌표_중복제거하여_조회() {
//
//        Integer distinctGridCount = 1632;
//
//        List<GridDto> gridDtos = weatherAreaRepository.findDistinctGridXY();
//
//        Assertions.assertThat(gridDtos.size()).isEqualTo(distinctGridCount);
//    }
}