package com.tftf.musictaggerserver;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tftf.musictaggerserver.dto.PlaytimeHistoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Repository
public class PlaytimeHistoryDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PlaytimeHistoryDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(String email, int musicId, JsonObject tagInfo) {
        String insertMemberSQL = "Insert Into playtimeHistoryTable Values(?, ?, ?)";
        jdbcTemplate.update(insertMemberSQL, email, musicId, tagInfo.toString());
    }

    public void update(String email, int musicId, JsonObject tagInfo) {
        String updateMemberSQL = "Update playtimeHistoryTable Set tagInfo = ? Where (email, musicId) = (?, ?)";
        jdbcTemplate.update(updateMemberSQL, tagInfo.toString(), email, musicId);
    }

    public void delete(String email, int musicId) {
        String deleteMemberSQL = "Delete From playtimeHistoryTable Where (email, musicId) = (?, ?)";
        jdbcTemplate.update(deleteMemberSQL, email, musicId);
    }

    class PlaytimeHistoryMapper implements RowMapper<PlaytimeHistoryDTO> {
        @Override
        public PlaytimeHistoryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PlaytimeHistoryDTO(rs.getString("email"), rs.getInt("musicId"), (JsonObject) JsonParser.parseString(rs.getString("tagInfo")));
        }
    }
    public JsonObject select(String email, int musicId) {
        String selectPlaytimeHistorySQL = "Select * From playtimeHistoryTable Where (email, musicId) = (?, ?)";

        try {
            return jdbcTemplate.queryForObject(selectPlaytimeHistorySQL, new PlaytimeHistoryMapper(), email, musicId).getTagInfo();
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }
    public HashMap<Integer, JsonObject> select(String email) {
        String selectPlaytimeHistorySQL = "Select * From playtimeHistoryTable Where email = ?";

        try {
            List<PlaytimeHistoryDTO> historyList = jdbcTemplate.query(selectPlaytimeHistorySQL, new PlaytimeHistoryMapper(), email);
            HashMap<Integer, JsonObject> joMap = new HashMap<>();
            for(PlaytimeHistoryDTO dto : historyList) {
                joMap.put(dto.getMusicId(), dto.getTagInfo());
            }
            return joMap;
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public List<PlaytimeHistoryDTO> selectAll() {
        String selectAllMemberSQL = "Select * From playtimeHistoryTable";
        return jdbcTemplate.query(selectAllMemberSQL, new PlaytimeHistoryMapper());
    }
}