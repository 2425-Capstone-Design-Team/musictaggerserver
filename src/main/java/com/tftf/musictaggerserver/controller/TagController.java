package com.tftf.musictaggerserver.controller;

import com.tftf.musictaggerserver.db.PlayHistoryDAO;
import com.tftf.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value="tag")
public class TagController {
    @Autowired
    PlayHistoryDAO playHistoryDAO;

    @PostMapping("personal")
    public @ResponseBody MusicTag getPersonalMusicTag(@RequestParam String userID, @RequestParam int musicID) {

        PlayHistory history = playHistoryDAO.select(userID, musicID);

        return MusicTagger.getMusicTag(history);
    }

    @PostMapping("general")
    public @ResponseBody MusicTag getGeneralMusicTag(@RequestParam int musicID) {

        List<PlayHistory> historyList = playHistoryDAO.select(musicID);

        return MusicTagger.getMusicTag(historyList);
    }
}
