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
    public void insert(@RequestParam("email") String email, @RequestParam("musicId") int musicId, @RequestBody JSONObject tagInfo) {
        playtimeHistoryDAO.insert(email, musicId, tagInfo.toJSONString());
    }

    @PostMapping(value="/delete")
    public void delete(@RequestParam("email") String email, @RequestParam("musicId") int musicId) {
        playtimeHistoryDAO.delete(email, musicId);
    }

    @PostMapping(value="/update")
    public void update(@RequestParam("email") String email, @RequestParam("musicId") int musicId, @RequestBody JSONObject tagInfo) {
        playtimeHistoryDAO.update(email, musicId, tagInfo.toJSONString());
    }

    @PostMapping(value="/select")
    public @ResponseBody String select(@RequestParam("email") String email, @RequestParam("musicId") int musicId) {
        String ret = playtimeHistoryDAO.select(email, musicId);
        if (ret == null) ret = "";
        return ret;
    }

    @PostMapping(value="/selectAll")
    public @ResponseBody List<PlaytimeHistoryDTO> selectAll() {
        return playtimeHistoryDAO.selectAll();
    }
}