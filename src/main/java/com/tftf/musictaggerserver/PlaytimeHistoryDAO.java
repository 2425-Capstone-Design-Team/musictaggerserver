package com.tftf.musictaggerserver;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PlaytimeHistoryDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PlaytimeHistoryDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(String emailAndMusicId, String tagInfo) {
        String insertMemberSQL = "Insert Into playtimeHistoryTable Values(?, ?)";
        jdbcTemplate.update(insertMemberSQL, emailAndMusicId, tagInfo);
    }

    public void update(String emailAndMusicId, String tagInfo) {
        String updateMemberSQL = "Update playtimeHistoryTable Set tagInfo = ? Where emailAndMusicId = ?";
        jdbcTemplate.update(updateMemberSQL, tagInfo, emailAndMusicId);
    }

    public void delete(String emailAndMusicId) {
        String deleteMemberSQL = "Delete From playtimeHistoryTable Where emailAndMusicId = ?";
        jdbcTemplate.update(deleteMemberSQL, emailAndMusicId);
    }

    class PlaytimeHistoryMapper implements RowMapper<PlaytimeHistoryDTO> {
        @Override
        public PlaytimeHistoryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PlaytimeHistoryDTO(rs.getString("emailAndMusicId"), rs.getString("tagInfo"));
        }
    }
    public String select(String emailAndMusicId) {
        String selectPlaytimeHistorySQL = "Select * From playtimeHistoryTable Where emailAndMusicId = ?";

        try {
            return jdbcTemplate.queryForObject(selectPlaytimeHistorySQL, new PlaytimeHistoryMapper(), emailAndMusicId).tagInfo;
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public List<PlaytimeHistoryDTO> selectAll() {
        String selectAllMemberSQL = "Select * From playtimeHistoryTable";
        return jdbcTemplate.query(selectAllMemberSQL, new PlaytimeHistoryMapper());
    }
}