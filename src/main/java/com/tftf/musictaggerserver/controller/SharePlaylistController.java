package com.tftf.musictaggerserver.controller;

import com.tftf.musictaggerserver.db.SharePlaylistDAO;
import com.tftf.util.PlaylistForShare;
import com.tftf.util.PlaylistManagerDTO;
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
            sharePlaylistDAO.insert(selected);
        }
        else {
            sharePlaylistDAO.update(selected);
        }
    }

    @PostMapping(value="/download")
    public @ResponseBody List<PlaylistForShare> download() {
        return sharePlaylistDAO.selectAll();
    }


    @PostMapping(value="/delete")
    public void delete(@RequestParam("userID") String userID, @RequestParam("name") String name) {
        sharePlaylistDAO.delete(userID, name);
    }

    @PostMapping(value="/select", params={"userID"})
    public @ResponseBody PlaylistForShare select(@RequestParam("userID") String userID) {
        return sharePlaylistDAO.select(userID);
    }

    @PostMapping(value="/selectAll")
    public @ResponseBody List<PlaylistForShare> selectAll() {
        return sharePlaylistDAO.selectAll();
    }
}