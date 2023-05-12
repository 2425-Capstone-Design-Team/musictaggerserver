package com.tftf.musictaggerserver.controller;

import com.tftf.musictaggerserver.db.PlayHistoryDAO;
import com.tftf.util.PlayHistory;
import com.tftf.util.PlayInform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="playtime_history")
public class PlayHistoryController {
    @Autowired
    private PlayHistoryDAO playHistoryDAO;

    @PostMapping(value="/cumulate")
    public boolean insert(@RequestBody PlayInform inform) {
        PlayHistory history = playHistoryDAO.select(inform.getUserID(), inform.getMusicID());

        if (history == null) {
            history = new PlayHistory(inform.getUserID(), inform.getMusicID());
            history.cumulatePlayedTime(inform.getPlayedTime(), inform.getSurroundings());
            playHistoryDAO.insert(history);
        }
        else {
            history.cumulatePlayedTime(inform.getPlayedTime(), inform.getSurroundings());
            playHistoryDAO.update(history);
        }

        return true;
    }

/*
    @PostMapping(value="/delete")
    public void delete(@RequestParam("email") String email, @RequestParam("musicId") int musicId) {
        playHistoryDAO.delete(email, musicId);
    }

    @PostMapping(value="/select", params={"email","musicId"})
    public @ResponseBody PlaytimeHistoryDTO select(@RequestParam("email") String email, @RequestParam("musicId") int musicId) {
        return playHistoryDAO.select(email, musicId);
    }

    @PostMapping(value="/select", params={"email"})
    public @ResponseBody List<PlaytimeHistoryDTO> select(@RequestParam("email") String email) {
        return playHistoryDAO.select(email);
    }

    @PostMapping(value="/selectAll")
    public @ResponseBody List<PlaytimeHistoryDTO> selectAll() {
        return playHistoryDAO.selectAll();
    }
*/

}