package com.fit.fit_be.config;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.like.model.Likes;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsertBoard(List<Board> boards) {
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

    public void batchInsertLike(List<Likes> likes) {
        String sql = "INSERT INTO likes (deleted, board_id, created_at, member_id, " +
                "updated_at) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Likes like = likes.get(i);
                ps.setBoolean(1, like.isDeleted());
                ps.setLong(2, like.getBoard().getId());
                ps.setDate(3, Date.valueOf(LocalDateTime.now().toLocalDate()));
                ps.setLong(4, like.getMember().getId());
                ps.setDate(5, Date.valueOf(LocalDateTime.now().toLocalDate()));
            }

            @Override
            public int getBatchSize() {
                return likes.size();
            }
        });
    }
}
