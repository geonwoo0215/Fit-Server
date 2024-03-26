package com.fit.fit_be.config;

import com.fit.fit_be.domain.board.model.Board;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class DataLoader {

    private JdbcTemplate jdbcTemplate;

    public DataLoader(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void batchInsert(List<Board> boards) {
        String sql = "INSERT INTO board (deleted, like_count, open, highest_temperature, " +
                "lowest_temperature, member_id, content, place, road_condition, weather) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Board board = boards.get(i);
                ps.setBoolean(1, board.isDeleted());
                ps.setInt(2, board.getLikeCount());
                ps.setBoolean(3, board.isOpen());
                ps.setLong(4, board.getHighestTemperature());
                ps.setLong(5, board.getLowestTemperature());
                ps.setLong(6, board.getMember().getId());
                ps.setString(7, board.getContent());
                ps.setString(8, board.getPlace().toString());
                ps.setString(9, board.getRoadCondition().toString());
                ps.setString(10, board.getWeather().toString());
            }

            @Override
            public int getBatchSize() {
                return boards.size();
            }
        });
    }
}
