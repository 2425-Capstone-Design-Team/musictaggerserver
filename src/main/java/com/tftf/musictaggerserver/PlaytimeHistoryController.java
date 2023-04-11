package com.tftf.musictaggerserver;

import com.google.gson.JsonObject;
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

    @PostMapping(value="/insert")
    public void insert(@RequestParam("email") String email, @RequestParam("musicId") int musicId, @RequestBody JsonObject tagInfo) {
        playtimeHistoryDAO.insert(email, musicId, tagInfo);
    }

    @PostMapping(value="/delete")
    public void delete(@RequestParam("email") String email, @RequestParam("musicId") int musicId) {
        playtimeHistoryDAO.delete(email, musicId);
    }

    @PostMapping(value="/update")
    public void update(@RequestParam("email") String email, @RequestParam("musicId") int musicId, @RequestBody JsonObject tagInfo) {
        playtimeHistoryDAO.update(email, musicId, tagInfo);
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