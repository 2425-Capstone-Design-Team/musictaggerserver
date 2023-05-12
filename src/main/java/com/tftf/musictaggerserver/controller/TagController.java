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
    public @ResponseBody List<MusicTag> getPersonalMusicTagList(@RequestParam String userID, @RequestParam List<Integer> musicIDList) {

        List<MusicTag> tagList = new ArrayList<>();
        for (int musicID : musicIDList) {
            PlayHistory history = playHistoryDAO.select(userID, musicID);

            if (history == null) {
                tagList.add(new MusicTag());
            }
            else {
                tagList.add(MusicTagger.getMusicTag(history));
            }
        }

        return tagList;
    }

    @PostMapping("general")
    public @ResponseBody List<MusicTag> getGeneralMusicTagList(@RequestParam List<Integer> musicIDList) {

        List<MusicTag> tagList = new ArrayList<>();
        for (int musicID : musicIDList) {
            List<PlayHistory> historyList = playHistoryDAO.select(musicID);
            tagList.add(MusicTagger.getMusicTag(historyList));
        }

        return tagList;
    }
}
