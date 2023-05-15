package com.tftf.musictaggerserver.db;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.tftf.util.JsonConverter;
import com.tftf.util.Playlist;
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
public class PlaylistDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PlaylistDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(Playlist playlist) {
        String insertSQL = "Insert Into user_playlist_table Values(?, ?, ?, ?)";
        jdbcTemplate.update(insertSQL,
                playlist.userID,
                playlist.name,
                playlist.description,
                JsonConverter.getJsonFromIntList(playlist.musicIDList).toString()
        );
    }

    public void update(Playlist playlist) {
        String updateSQL = "Update user_playlist_table Set description = ?, musicID_list = ? Where (userID, name) = (?, ?)";
        jdbcTemplate.update(updateSQL,
                playlist.description,
                JsonConverter.getJsonFromIntList(playlist.musicIDList).toString(),
                playlist.userID,
                playlist.name
        );
    }

    public void delete(String userID, String name) {
        String deleteSQL = "Delete From user_playlist_table Where (userID, name) = (?, ?)";
        jdbcTemplate.update(deleteSQL, userID, name);
    }

    class PlaylistMapper implements RowMapper<Playlist> {
        @Override
        public Playlist mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Playlist(
                    rs.getString("userID"),
                    rs.getString("name"),
                    rs.getString("description"),
                    JsonConverter.getIntListFromJson(JsonParser.parseString(rs.getString("musicID_list")).getAsJsonArray())
            );
        }
    }

    public Playlist select(String userID, String name) {
        String selectSQL = "Select * From user_playlist_table Where (userID, name) = (?, ?)";

        try {
            return jdbcTemplate.queryForObject(selectSQL, new PlaylistMapper(), userID, name);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public List<Playlist> selectByUserID(String userID) {
        String selectSQL = "Select * From user_playlist_table Where userID = ? Order By name";

        try {
            return jdbcTemplate.query(selectSQL, new PlaylistMapper(), userID);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public List<Playlist> selectByName(String name) {
        String selectSQL = "Select * From user_playlist_table Where name = ?";

        try {
            return jdbcTemplate.query(selectSQL, new PlaylistMapper(), name);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public List<Playlist> selectAll() {
        String selectAllSQL = "Select * From user_playlist_table";
        return jdbcTemplate.query(selectAllSQL, new PlaylistMapper());
    }
}