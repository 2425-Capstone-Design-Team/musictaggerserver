package com.tftf.musictaggerserver;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="playtimehistory")
public class PlaytimeHistoryController {
    @Autowired
    private PlaytimeHistoryDAO playtimeHistoryDAO;

    @PostMapping(value="/insert")
    public void insert(@RequestParam("emailAndMusicId") String emailAndMusicId, @RequestBody JSONObject tagInfo) {
        playtimeHistoryDAO.insert(emailAndMusicId, tagInfo);
    }

    @PostMapping(value="/delete")
    public void delete(@RequestParam("emailAndMusicId") String emailAndMusicId) {
        playtimeHistoryDAO.delete(emailAndMusicId);
    }

    @PostMapping(value="/update")
    public void update(@RequestParam("emailAndMusicId") String emailAndMusicId, @RequestParam("tagInfo") String tagInfo) {
        playtimeHistoryDAO.update(emailAndMusicId, tagInfo);
    }

    @PostMapping(value="/select")
    public @ResponseBody String select(@RequestParam("emailAndMusicId") String emailAndMusicId) {
        return playtimeHistoryDAO.select(emailAndMusicId);
    }

    @PostMapping(value="/selectAll")
    public @ResponseBody List<PlaytimeHistoryDTO> selectAll() {
        return playtimeHistoryDAO.selectAll();
    }
}