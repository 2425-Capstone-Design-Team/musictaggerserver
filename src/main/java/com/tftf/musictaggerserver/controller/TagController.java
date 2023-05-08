package com.tftf.musictaggerserver.controller;

import com.tftf.musictaggerserver.db.PlaytimeHistoryDAO;
import com.tftf.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.CharSequence.compare;


@RestController
@RequestMapping(value="tag")
public class TagController {
    @Autowired
    PlaytimeHistoryDAO playtimeHistoryDAO;

    @PostMapping("personal")
    public @ResponseBody MusicTag getPersonalMusicTag(@RequestParam("email") String email, @RequestParam("id") int musicID) {

        PlaytimeHistoryDTO dto = playtimeHistoryDAO.select(email, musicID);
        PlayHistory history = new PlayHistory();
        history.importFromJson(dto.getHistoryJO());

        return MusicTagger.getMusicTag(history);
    }

    @PostMapping("general")
    public @ResponseBody MusicTag getGeneralMusicTag(@RequestParam("id") int musicID) {

        List<PlaytimeHistoryDTO> dtoList = playtimeHistoryDAO.select(musicID);

        ArrayList<PlayHistory> historyList = new ArrayList<>();
        for (PlaytimeHistoryDTO dto : dtoList) {
            PlayHistory history = new PlayHistory();
            history.importFromJson(dto.getHistoryJO());
            historyList.add(history);
        }

        return MusicTagger.getMusicTag(historyList);
    }
}
