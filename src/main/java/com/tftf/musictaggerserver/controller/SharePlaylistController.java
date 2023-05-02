package com.tftf.musictaggerserver.controller;

import com.tftf.musictaggerserver.db.SharePlaylistDAO;
import com.tftf.util.PlaylistForShareDTO;
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
    public void upload(@RequestBody PlaylistForShareDTO dto) {
        PlaylistForShareDTO loadedDto = sharePlaylistDAO.select(dto.getEmail(), dto.getName());

        if (loadedDto == null) {
            sharePlaylistDAO.insert(dto);
        }
        else {
            sharePlaylistDAO.update(dto);
        }
    }

    @PostMapping(value="/download")
    public @ResponseBody List<PlaylistForShareDTO> download() {
        return sharePlaylistDAO.selectAll();
    }


    @PostMapping(value="/delete")
    public void delete(@RequestParam("email") String email, @RequestParam("name") String name) {
        sharePlaylistDAO.delete(email, name);
    }

    @PostMapping(value="/select", params={"email"})
    public @ResponseBody PlaylistForShareDTO select(@RequestParam("email") String email) {
        return sharePlaylistDAO.select(email);
    }

    @PostMapping(value="/selectAll")
    public @ResponseBody List<PlaylistForShareDTO> selectAll() {
        return sharePlaylistDAO.selectAll();
    }
}