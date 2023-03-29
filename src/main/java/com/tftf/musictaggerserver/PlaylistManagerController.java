package com.tftf.musictaggerserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="playlist")
public class PlaylistManagerController {
    @Autowired
    private PlaylistManagerDAO playlistManagerDAO;

    @PostMapping(value="/insert")
    public void insert(@RequestBody PlaylistManagerDTO playlistManagerDTO) {
        playlistManagerDAO.insert(playlistManagerDTO);
    }

    @PostMapping(value="/delete")
    public void delete(@RequestParam("email") String email) {
        playlistManagerDAO.delete(email);
    }

    @PostMapping(value="/update")
    public void update(@RequestBody PlaylistManagerDTO playlistManagerDTO) {
        playlistManagerDAO.update(playlistManagerDTO);
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