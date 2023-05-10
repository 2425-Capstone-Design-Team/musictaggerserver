package com.tftf.musictaggerserver.controller;

import com.tftf.musictaggerserver.db.PlaylistDAO;
import com.tftf.util.Playlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="playlist")
public class PlaylistController {
    @Autowired
    private PlaylistDAO playlistDAO;

    @PostMapping(value="/save")
    public void save(@RequestBody Playlist playlist) {
        Playlist selected = playlistDAO.select(playlist.userID, playlist.name);

        if (selected == null) {
            playlistDAO.insert(playlist);
        }
        else {
            playlistDAO.update(playlist);
        }
    }

    @PostMapping(value="/delete")
    public void delete(@RequestParam String userID, @RequestParam String name) {
        playlistDAO.delete(userID, name);
    }

    @PostMapping(value="/select")
    public @ResponseBody Playlist select(@RequestParam String userID, @RequestParam String name) {
        return playlistDAO.select(userID, name);
    }

    @PostMapping(value="/select_by_userid")
    public @ResponseBody List<Playlist> selectByUserID(@RequestParam String userID) {
        return playlistDAO.selectByUserID(userID);
    }

    @PostMapping(value="/select_by_name")
    public @ResponseBody List<Playlist> selectByName(@RequestParam String name) {
        return playlistDAO.selectByUserID(name);
    }

    @PostMapping(value="/selectAll")
    public @ResponseBody List<Playlist> selectAll() {
        return playlistDAO.selectAll();
    }
}