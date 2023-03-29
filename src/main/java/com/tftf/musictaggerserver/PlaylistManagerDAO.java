package com.tftf.musictaggerserver;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PlaylistManagerDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PlaylistManagerDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(PlaylistManagerDTO playlistDTO) {
        String insertSQL = "Insert Into playlistTable Values(?, ?)";
        jdbcTemplate.update(insertSQL, playlistDTO.email, playlistDTO.musicIdList.toString());
        /*
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(
                        "Insert Into playlistTable Values(?, ?)"
                );
                pstmt.setString(1, playlistDTO.email);
                pstmt.setObject(2, playlistDTO.musicIdList);

                return pstmt;
            }
        });
        */
    }

    public void update(PlaylistManagerDTO playlistDTO) {
        String updateSQL = "Update playlistTable Set musicIdList = ? Where email = ?";
        jdbcTemplate.update(updateSQL, playlistDTO.musicIdList.toString(), playlistDTO.email);
    }

    public void delete(String email) {
        String deleteSQL = "Delete From playlistTable Where email = ?";
        jdbcTemplate.update(deleteSQL, email);
    }

    class PlaylistManagerMapper implements RowMapper<PlaylistManagerDTO> {
        @Override
        public PlaylistManagerDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PlaylistManagerDTO(rs.getString("email"), (JsonArray) JsonParser.parseString(rs.getString("musicIdList")));
        }
    }

    public PlaylistManagerDTO select(String email) {
        String selectSQL = "Select * From playlistTable Where email = ?";

        try {
            return jdbcTemplate.queryForObject(selectSQL, new PlaylistManagerMapper(), email);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public List<PlaylistManagerDTO> selectAll() {
        String selectAllSQL = "Select * From playlistTable";
        return jdbcTemplate.query(selectAllSQL, new PlaylistManagerMapper());
    }
}