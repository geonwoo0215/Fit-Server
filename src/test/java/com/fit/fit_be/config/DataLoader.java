package com.fit.fit_be.config;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.like.model.Likes;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataLoader {

    private JdbcTemplate jdbcTemplate;

    public DataLoader(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void batchInsertBoard(List<Board> boards) {
        String sql = "INSERT INTO board (deleted, like_count, open, highest_temperature, " +
                "lowest_temperature, member_id, content, place, road_condition, weather, created_at, ranking) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                ps.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now().minusDays(1L)));
                ps.setBoolean(12, board.isRanking());
            }

            @Override
            public int getBatchSize() {
                return boards.size();
            }
        });
    }

    public void batchInsertLike(List<Likes> likes) {
        String sql = "INSERT INTO likes (deleted, board_id, created_at, member_id, " +
                "updated_at, created_date ) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Likes like = likes.get(i);
                ps.setBoolean(1, like.isDeleted());
                ps.setLong(2, like.getBoard().getId());
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().minusDays(1L).minusHours(1)));
                ps.setLong(4, like.getMember().getId());
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now().minusDays(1L).minusHours(1)));
                ps.setDate(6, Date.valueOf(LocalDate.now().minusDays(1L)));
            }

            @Override
            public int getBatchSize() {
                return likes.size();
            }
        });
    }
}
