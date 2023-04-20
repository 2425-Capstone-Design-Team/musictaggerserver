package com.tftf.musictaggerserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.tftf.util.MusicPlayHistoryDTO;

@Repository
public class MusicPlayHistoryDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MusicPlayHistoryDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(MusicPlayHistoryDTO dto) {
        String insertSQL = "Insert Into musicTagInfoTable Values(?, ?)";
        jdbcTemplate.update(insertSQL, dto.getMusicId(), dto.getJsonArray().toString());
    }

    public void update(MusicPlayHistoryDTO dto) {
        String updateSQL = "Update musicTagInfoTable Set tagInfo = ? Where musicId = ?";
        jdbcTemplate.update(updateSQL, dto.getJsonArray().toString(), dto.getMusicId());
    }

    public void delete(int musicId) {
        String deleteSQL = "Delete From musicTagInfoTable Where musicId = ?";
        jdbcTemplate.update(deleteSQL, musicId);
    }

    class MusicPlayHistoryMapper implements RowMapper<MusicPlayHistoryDTO> {
        @Override
        public MusicPlayHistoryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MusicPlayHistoryDTO(rs.getInt("musicId"), (JsonArray) JsonParser.parseString(rs.getString("tagInfo")));
        }
    }

    public MusicPlayHistoryDTO select(int musicId) {
        String selectSQL = "Select * From musicTagInfoTable Where musicId = ?";

        try {
            return jdbcTemplate.queryForObject(selectSQL, new MusicPlayHistoryMapper(), musicId);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public List<MusicPlayHistoryDTO> selectAll() {
        String selectAllSQL = "Select * From musicTagInfoTable";
        return jdbcTemplate.query(selectAllSQL, new MusicPlayHistoryMapper());
    }
}