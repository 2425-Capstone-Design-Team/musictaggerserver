package com.tftf.musictaggerserver.controller;

import com.tftf.musictaggerserver.db.SharePlaylistDAO;
import com.tftf.util.PlaylistForShare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="share")
public class SharePlaylistController {
    @Autowired
    private SharePlaylistDAO sharePlaylistDAO;

    @PostMapping(value="/upload")
    public void upload(@RequestBody PlaylistForShare playlist) {
        PlaylistForShare selected = sharePlaylistDAO.select(playlist.userID, playlist.name);

        if (selected == null) {
            sharePlaylistDAO.insert(playlist);
        }
        else {
            sharePlaylistDAO.update(playlist);
        }
    }

    @PostMapping(value="/delete")
    public void delete(@RequestParam("userID") String userID, @RequestParam String name) {
        sharePlaylistDAO.delete(userID, name);
    }

    @PostMapping(value="/select")
    public @ResponseBody PlaylistForShare select(@RequestParam String userID, @RequestParam String name) {
        return sharePlaylistDAO.select(userID, name);
    }

    @PostMapping(value="/select_by_userid")
    public @ResponseBody List<PlaylistForShare> selectByUserID(@RequestParam String userID) {
        return sharePlaylistDAO.selectByUserID(userID);
    }

    @PostMapping(value="/select_by_name")
    public @ResponseBody List<PlaylistForShare> selectByName(@RequestParam String name) {
        return sharePlaylistDAO.selectByUserID(name);
    }

    @PostMapping(value="/selectAll")
    public @ResponseBody List<PlaylistForShare> selectAll() {
        return sharePlaylistDAO.selectAll();
    }
}