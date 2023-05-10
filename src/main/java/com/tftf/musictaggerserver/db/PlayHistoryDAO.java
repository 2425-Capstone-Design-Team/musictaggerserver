package com.tftf.musictaggerserver.db;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tftf.util.JsonConverter;
import com.tftf.util.PlayHistory;
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
public class PlayHistoryDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PlayHistoryDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(PlayHistory history) {
        String insertMemberSQL = "Insert Into playtime_history_table Values(?, ?, ?, ?)";
        jdbcTemplate.update(insertMemberSQL, history.userID, history.musicID, history.totalPlayedTime, JsonConverter.getJsonFromPlayHistory(history).toString());
    }

    public void update(PlayHistory history) {
        String updateMemberSQL = "Update playtime_history_table Set total_played_time = ?, tag_info = ? Where (userID, musicID) = (?, ?)";
        jdbcTemplate.update(updateMemberSQL, history.totalPlayedTime, JsonConverter.getJsonFromPlayHistory(history).toString(), history.userID, history.musicID);
    }

    public void delete(String userID, int musicID) {
        String deleteMemberSQL = "Delete From playtime_history_table Where (userID, musicID) = (?, ?)";
        jdbcTemplate.update(deleteMemberSQL, userID, musicID);
    }

    static class PlayHistoryMapper implements RowMapper<PlayHistory> {
        @Override
        public PlayHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PlayHistory(rs.getString("userID"), rs.getInt("musicID"),rs.getLong("total_played_time") , (JsonObject) JsonParser.parseString(rs.getString("tag_info")));
        }
    }
    public PlayHistory select(String userID, int musicID) {
        String selectPlaytimeHistorySQL = "Select * From playtime_history_table Where (userID, musicID) = (?, ?)";

        try {
            return jdbcTemplate.queryForObject(selectPlaytimeHistorySQL, new PlayHistoryMapper(), userID, musicID);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }
    public List<PlayHistory> select(String userID) {
        String selectPlaytimeHistorySQL = "Select * From playtime_history_table Where userID = ?";

        try {
            return jdbcTemplate.query(selectPlaytimeHistorySQL, new PlayHistoryMapper(), userID);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public List<PlayHistory> select(int musicID) {
        String selectPlaytimeHistorySQL = "Select * From playtime_history_table Where musicID = ?";

        try {
            return jdbcTemplate.query(selectPlaytimeHistorySQL, new PlayHistoryMapper(), musicID);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public List<PlayHistory> selectAll() {
        String selectAllMemberSQL = "Select * From playtime_history_table";
        return jdbcTemplate.query(selectAllMemberSQL, new PlayHistoryMapper());
    }
}