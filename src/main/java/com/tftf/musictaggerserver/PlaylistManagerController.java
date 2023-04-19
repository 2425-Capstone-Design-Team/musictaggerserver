package com.tftf.musictaggerserver;

import com.google.gson.JsonObject;
import com.tftf.musictaggerserver.dto.PlaylistManagerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="playlist")
public class PlaylistManagerController {
    @Autowired
    private PlaylistManagerDAO playlistManagerDAO;

    @PostMapping(value="/save")
    public void save(@RequestBody PlaylistManagerDTO dto) {
        PlaylistManagerDTO loadedDto = playlistManagerDAO.select(dto.getEmail());

        if (loadedDto == null) {
            playlistManagerDAO.insert(dto);
        }
        else {
            playlistManagerDAO.update(dto);
        }
    }

    @PostMapping(value="/delete")
    public void delete(@RequestParam("email") String email) {
        playlistManagerDAO.delete(email);
    }

    @PostMapping(value="/select", params={"email"})
    public @ResponseBody PlaylistManagerDTO select(@RequestParam("email") String email) {
        return playlistManagerDAO.select(email);
    }


    @PostMapping(value="/selectAll")
    public @ResponseBody List<PlaylistManagerDTO> selectAll() {
        return playlistManagerDAO.selectAll();
    }
}