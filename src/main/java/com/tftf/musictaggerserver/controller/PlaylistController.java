package com.tftf.musictaggerserver.controller;

import com.tftf.musictaggerserver.db.PlaylistDAO;
import com.tftf.util.Playlist;
import com.tftf.util.PlaylistManagerDTO;
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
    public void delete(@RequestParam("userID") String userID, @RequestParam("name") String name) {
        playlistDAO.delete(userID, name);
    }

    @PostMapping(value="/select", params={"userID"})
    public @ResponseBody Playlist select(@RequestParam("userID") String userID, @RequestParam("name") String name) {
        return playlistDAO.select(userID, name);
    }

    @PostMapping(value="/selectAll")
    public @ResponseBody List<Playlist> selectAll() {
        return playlistDAO.selectAll();
    }
}