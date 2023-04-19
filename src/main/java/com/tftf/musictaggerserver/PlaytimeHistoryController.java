package com.tftf.musictaggerserver;

import com.google.gson.JsonObject;
import com.tftf.musictaggerserver.dto.PlaytimeHistoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value="playtimehistory")
public class PlaytimeHistoryController {
    @Autowired
    private PlaytimeHistoryDAO playtimeHistoryDAO;
    @Autowired
    private MusicPlayHistoryDAO musicPlayHistoryDAO;

    @PostMapping(value="/save")
    public void insert(@RequestParam("email") String email, @RequestParam("musicId") int musicId, @RequestBody JsonObject tagInfo) {
        JsonObject jo = playtimeHistoryDAO.select(email, musicId);

        if (jo == null) {
            playtimeHistoryDAO.insert(email, musicId, tagInfo);
        }
        else {
            playtimeHistoryDAO.update(email, musicId, tagInfo);
        }
    }

    @PostMapping(value="/delete")
    public void delete(@RequestParam("email") String email, @RequestParam("musicId") int musicId) {
        playtimeHistoryDAO.delete(email, musicId);
    }

    @PostMapping(value="/select", params={"email","musicId"})
    public @ResponseBody JsonObject select(@RequestParam("email") String email, @RequestParam("musicId") int musicId) {
        return playtimeHistoryDAO.select(email, musicId);
    }

    @PostMapping(value="/select", params={"email"})
    public @ResponseBody HashMap<Integer, JsonObject> select(@RequestParam("email") String email) {
        return playtimeHistoryDAO.select(email);
    }


    @PostMapping(value="/selectAll")
    public @ResponseBody List<PlaytimeHistoryDTO> selectAll() {
        return playtimeHistoryDAO.selectAll();
    }
}