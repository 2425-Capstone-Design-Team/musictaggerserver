package com.tftf.musictaggerserver.controller;

import com.google.gson.JsonObject;
import com.tftf.musictaggerserver.db.PlaytimeHistoryDAO;
import com.tftf.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static java.lang.CharSequence.compare;

@RestController
@RequestMapping(value="recommend")
public class RecommendationController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    PlaytimeHistoryDAO playtimeHistoryDAO;
    
    // todo : 태그정보 받아와서 추천리스트 만들고 반환해주기
    @PostMapping("personalized")
    public @ResponseBody List<Integer> getPersonalizedList(@RequestParam("email") String email,
                                                           @RequestBody Surroundings surroundings,
                                                           @RequestParam int listSize) {

        List<PlaytimeHistoryDTO> histories = playtimeHistoryDAO.select(email);

        // PriorityQueue<Pair<MusicID, Point>>, 내림차순
        PriorityQueue<Pair<Integer, Integer>> PQ = new PriorityQueue<>(listSize,
                (p1, p2) -> p2.getSecond() - p1.getSecond());

        for (PlaytimeHistoryDTO historyDto : histories) {
            PlayHistory history = new PlayHistory();
            history.importFromJson(historyDto.getHistoryJO());
            MusicTag historyMusicTag = MusicTagger.getMusicTag(history);

            int point = 0;
            for (CharSequence category : surroundings.infoMap.keySet()) {
                CharSequence surroundingsInfo = surroundings.infoMap.get(category);
                CharSequence historyMusicTagInfo = historyMusicTag.tagMap.get(category);

                if (compare(historyMusicTagInfo, surroundingsInfo) == 0) {
                    point++;
                }
            }

            PQ.add(new Pair<>(historyDto.getMusicId(), point));
        }

        ArrayList<Integer> personalizedList = new ArrayList<>();

        for (Pair<Integer, Integer> p : PQ) {
            if (listSize-- == 0) break;
            personalizedList.add(p.getFirst());
        }

        return personalizedList;
    }

    @PostMapping("generalized")
    public @ResponseBody List<Integer> getGeneralizedList(@RequestBody Surroundings surroundings,
                                                           @RequestParam int listSize) {

        List<PlaytimeHistoryDTO> histories = playtimeHistoryDAO.selectAll();

        // PriorityQueue<Pair<MusicID, Point>>, 내림차순
        PriorityQueue<Pair<Integer, Integer>> PQ = new PriorityQueue<>(listSize,
                (p1, p2) -> p2.getSecond() - p1.getSecond());

        for (PlaytimeHistoryDTO historyDto : histories) {
            PlayHistory history = new PlayHistory();
            history.importFromJson(historyDto.getHistoryJO());
            MusicTag historyMusicTag = MusicTagger.getMusicTag(history);

            int point = 0;
            for (CharSequence category : surroundings.infoMap.keySet()) {
                CharSequence surroundingsInfo = surroundings.infoMap.get(category);
                CharSequence historyMusicTagInfo = historyMusicTag.tagMap.get(category);

                if (compare(historyMusicTagInfo, surroundingsInfo) == 0) {
                    point++;
                }
            }

            PQ.add(new Pair<>(historyDto.getMusicId(), point));
        }

        ArrayList<Integer>  generalizedList = new ArrayList<>();

        for (Pair<Integer, Integer> p : PQ) {
            if (listSize-- == 0) break;
            generalizedList.add(p.getFirst());
        }

        return generalizedList;
    }

    @PostMapping("theme")
    public @ResponseBody List<Playlist> getThemeList(@RequestBody Surroundings surroundings,
                                               @RequestParam int listSize) {

        List<Playlist> ListOfPlaylist = new ArrayList<>();
        List<PlaytimeHistoryDTO> historyDtoList = playtimeHistoryDAO.selectAll();

        for (CharSequence category : surroundings.infoMap.keySet()) {
            CharSequence surroundingsInfo = surroundings.infoMap.get(category);

            // PriorityQueue<Pair<MusicID, Point>>, 내림차순
            PriorityQueue<Pair<Integer, Long>> PQ = new PriorityQueue<>(listSize,
                    (p1, p2) -> Math.toIntExact(p2.getSecond() - p1.getSecond()));

            for (PlaytimeHistoryDTO dto : historyDtoList) {
                PlayHistory history = new PlayHistory();
                history.importFromJson(dto.getHistoryJO());

                Long point = history.getPlaytime(category, surroundingsInfo);

                PQ.add(new Pair<>(dto.getMusicId(), point));
            }


            Playlist playlist = new Playlist(surroundingsInfo.toString(), new ArrayList<>());
            int sz = listSize;
            for (Pair<Integer, Long> p : PQ) {
                if (sz-- == 0) break;
                playlist.getMusicList().add(p.getFirst());
            }
            ListOfPlaylist.add(playlist);
        }

        return ListOfPlaylist;
    }

    @PostMapping("toprank")
    public @ResponseBody Playlist getTopRankList(@RequestParam int listSize) {

        List<PlaytimeHistoryDTO> historyDtoList = playtimeHistoryDAO.selectAll();

        PriorityQueue<Pair<Integer, Long>> PQ = new PriorityQueue<>(listSize,
                (p1, p2) -> Math.toIntExact(p2.getSecond() - p1.getSecond()));

        for (PlaytimeHistoryDTO dto : historyDtoList) {
            PQ.add(new Pair<>(dto.getMusicId(), dto.getTotalPlaytime()));
        }

        Playlist playlist = new Playlist("탑"+listSize, new ArrayList<>());
        for (Pair<Integer, Long> p : PQ) {
            if (listSize-- == 0) break;
            playlist.getMusicList().add(p.getFirst());
        }

        return playlist;
    }
}
