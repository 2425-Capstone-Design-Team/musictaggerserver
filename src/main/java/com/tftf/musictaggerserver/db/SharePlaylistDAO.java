package com.tftf.musictaggerserver.db;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tftf.util.PlaylistForShareDTO;
import com.tftf.util.PlaylistManagerDTO;
import com.tftf.util.PlaytimeHistoryDTO;
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
public class SharePlaylistDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SharePlaylistDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(PlaylistForShareDTO dto) {
        String insertSQL = "Insert Into playlistsharetable Values(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertSQL,
                dto.getEmail(),
                dto.getName(),
                dto.getMusicListJson().toString(),
                dto.getDescription(),
                dto.getLikeCount(),
                dto.getCopyCount()
        );
    }

    public void update(PlaylistForShareDTO dto) {
        String updateSQL = "Update playlistsharetable Set musicIdList = ?, description = ?, likeCount = ?, copyCount = ? Where (email, name) = (?, ?)";
        jdbcTemplate.update(updateSQL,
                dto.getMusicListJson().toString(),
                dto.getDescription(),
                dto.getLikeCount(),
                dto.getCopyCount(),
                dto.getEmail(),
                dto.getName());
    }

    public void delete(String email, String name) {
        String deleteSQL = "Delete From playlistsharetable Where (email, name) = (?, ?)";
        jdbcTemplate.update(deleteSQL, email, name);
    }

    class PlaylistForShareMapper implements RowMapper<PlaylistForShareDTO> {
        @Override
        public PlaylistForShareDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PlaylistForShareDTO(
                    rs.getString("name"),
                    (JsonArray) JsonParser.parseString(rs.getString("musicIdList")),
                    rs.getString("email"),
                    rs.getString("description"),
                    rs.getInt("likeCount"),
                    rs.getInt("copyCount")
                    );
        }
    }

    public PlaylistForShareDTO select(String email) {
        String selectSQL = "Select * From playlistsharetable Where email = ?";

        try {
            return jdbcTemplate.queryForObject(selectSQL, new PlaylistForShareMapper(), email);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public PlaylistForShareDTO select(String email, String name) {
        String selectSQL = "Select * From playlistsharetable Where (email, name) = (?, ?)";

        try {
            return jdbcTemplate.queryForObject(selectSQL, new PlaylistForShareMapper(), email, name);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public List<PlaylistForShareDTO> selectAll() {
        String selectAllSQL = "Select * From playlistsharetable";
        return jdbcTemplate.query(selectAllSQL, new PlaylistForShareMapper());
    }
}