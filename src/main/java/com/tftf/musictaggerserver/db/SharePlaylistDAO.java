package com.tftf.musictaggerserver.db;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tftf.util.JsonConverter;
import com.tftf.util.PlaylistForShare;
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

    public void insert(PlaylistForShare playlist) {
        String insertSQL = "Insert Into shared_playlist_table Values(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertSQL,
                playlist.userID,
                playlist.name,
                playlist.description,
                JsonConverter.getJsonFromIntList(playlist.musicList).toString(),
                playlist.likeCount,
                playlist.downloadCount
        );
    }

    public void update(PlaylistForShare playlist) {
        String updateSQL = "Update shared_playlist_table Set description = ?, musicID_list = ?, like_count = ?, download_count = ? Where (userID, name) = (?, ?)";
        jdbcTemplate.update(updateSQL,
                playlist.description,
                JsonConverter.getJsonFromIntList(playlist.musicList).toString(),
                playlist.likeCount,
                playlist.downloadCount,
                playlist.userID,
                playlist.name
        );
    }

    public void delete(String email, String name) {
        String deleteSQL = "Delete From shared_playlist_table Where (userID, name) = (?, ?)";
        jdbcTemplate.update(deleteSQL, email, name);
    }

    class PlaylistForShareMapper implements RowMapper<PlaylistForShare> {
        @Override
        public PlaylistForShare mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PlaylistForShare(
                    rs.getString("userID"),
                    rs.getString("name"),
                    rs.getString("description"),
                    JsonConverter.getIntListFromJson(JsonParser.parseString(rs.getString("musicId_list")).getAsJsonArray()),
                    rs.getInt("like_count"),
                    rs.getInt("download_count")
                    );
        }
    }

    public PlaylistForShare select(String userID, String name) {
        String selectSQL = "Select * From shared_playlist_table Where (userID, name) = (?, ?)";

        try {
            return jdbcTemplate.queryForObject(selectSQL, new PlaylistForShareMapper(), userID, name);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }
    public List<PlaylistForShare> selectByUserID(String userID) {
        String selectSQL = "Select * From shared_playlist_table Where userID = ?";

        try {
            return jdbcTemplate.query(selectSQL, new PlaylistForShareMapper(), userID);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public List<PlaylistForShare> selectByName(String name) {
        String selectSQL = "Select * From shared_playlist_table Where name = ?";

        try {
            return jdbcTemplate.query(selectSQL, new PlaylistForShareMapper(), name);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    public List<PlaylistForShare> selectAll() {
        String selectAllSQL = "Select * From shared_playlist_table";
        return jdbcTemplate.query(selectAllSQL, new PlaylistForShareMapper());
    }
}