package com.tftf.musictaggerserver;

import com.google.gson.JsonObject;
import com.tftf.util.PlaytimeHistoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value="playtimehistory")
public class PlaytimeHistoryController {
    @Autowired
    private PlaytimeHistoryDAO playtimeHistoryDAO;

    @PostMapping(value="/save")
    public void insert(@RequestBody PlaytimeHistoryDTO dto) {
        PlaytimeHistoryDTO loadedDto = playtimeHistoryDAO.select(dto.getEmail(), dto.getMusicId());

        if (loadedDto == null) {
            playtimeHistoryDAO.insert(dto);
        }
        else {
            playtimeHistoryDAO.update(dto);
        }
    }

    @PostMapping(value="/delete")
    public void delete(@RequestParam("email") String email, @RequestParam("musicId") int musicId) {
        playtimeHistoryDAO.delete(email, musicId);
    }

    @PostMapping(value="/select", params={"email","musicId"})
    public @ResponseBody PlaytimeHistoryDTO select(@RequestParam("email") String email, @RequestParam("musicId") int musicId) {
        return playtimeHistoryDAO.select(email, musicId);
    }

    @PostMapping(value="/select", params={"email"})
    public @ResponseBody List<PlaytimeHistoryDTO> select(@RequestParam("email") String email) {
        return playtimeHistoryDAO.select(email);
    }

    @PostMapping(value="/selectAll")
    public @ResponseBody List<PlaytimeHistoryDTO> selectAll() {
        return playtimeHistoryDAO.selectAll();
    }
}