package com.tftf.musictaggerserver.controller;

import com.tftf.musictaggerserver.db.PlayHistoryDAO;
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
    PlayHistoryDAO playHistoryDAO;
    
    // todo : 태그정보 받아와서 추천리스트 만들고 반환해주기
    @PostMapping("personalized")
    public @ResponseBody List<Integer> getPersonalizedList(@RequestParam String userID,
                                                           @RequestBody Surroundings surroundings,
                                                           @RequestParam int listSize) {

        List<PlayHistory> historyList = playHistoryDAO.select(userID);

        // PriorityQueue<Pair<MusicID, Point>>, 내림차순
        PriorityQueue<Pair<Integer, Integer>> PQ = new PriorityQueue<>(listSize,
                (p1, p2) -> p2.getSecond() - p1.getSecond());

        for (PlayHistory history : historyList) {
            MusicTag historyMusicTag = MusicTagger.getMusicTag(history);

            int score = 0;
            for (CharSequence category : surroundings.infoMap.keySet()) {
                CharSequence surroundingsInfo = surroundings.infoMap.get(category);
                CharSequence historyMusicTagInfo = historyMusicTag.tagMap.get(category);

                if (compare(historyMusicTagInfo, surroundingsInfo) == 0) {
                    score++;
                }
            }

            PQ.add(new Pair<>(history.musicID, score));
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

        List<PlayHistory> historyList = playHistoryDAO.selectAll();

        // PriorityQueue<Pair<MusicID, Point>>, 내림차순
        PriorityQueue<Pair<Integer, Integer>> PQ = new PriorityQueue<>(listSize,
                (p1, p2) -> p2.getSecond() - p1.getSecond());

        for (PlayHistory history : historyList) {
            MusicTag historyMusicTag = MusicTagger.getMusicTag(history);

            int score = 0;
            for (CharSequence category : surroundings.infoMap.keySet()) {
                CharSequence surroundingsInfo = surroundings.infoMap.get(category);
                CharSequence historyMusicTagInfo = historyMusicTag.tagMap.get(category);

                if (compare(historyMusicTagInfo, surroundingsInfo) == 0) {
                    score++;
                }
            }

            PQ.add(new Pair<>(history.musicID, score));
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

        List<Playlist> ThemePlaylist = new ArrayList<>();
        List<PlayHistory> historyList = playHistoryDAO.selectAll();

        for (CharSequence category : surroundings.infoMap.keySet()) {
            CharSequence surroundingsInfo = surroundings.infoMap.get(category);

            // PriorityQueue<Pair<MusicID, Point>>, 내림차순
            PriorityQueue<Pair<Integer, Long>> PQ = new PriorityQueue<>(listSize,
                    (p1, p2) -> Math.toIntExact(p2.getSecond() - p1.getSecond()));

            for (PlayHistory history : historyList) {
                Long score = history.getPlayedTime(category, surroundingsInfo);

                PQ.add(new Pair<>(history.musicID, score));
            }


            Playlist playlist = new Playlist(
                    "server",
                    surroundingsInfo.toString(),
                    surroundingsInfo.toString() + " 테마 추천 플레이리스트입니다.",
                    new ArrayList<>()
            );
            int sz = listSize;
            for (Pair<Integer, Long> p : PQ) {
                if (sz-- == 0) break;
                playlist.musicList.add(p.getFirst());
            }
            ThemePlaylist.add(playlist);
        }

        return ThemePlaylist;
    }

    @PostMapping("toprank")
    public @ResponseBody Playlist getTopRankList(@RequestParam int listSize) {

        List<PlayHistory> historyList = playHistoryDAO.selectAll();

        PriorityQueue<Pair<Integer, Long>> PQ = new PriorityQueue<>(listSize,
                (p1, p2) -> Math.toIntExact(p2.getSecond() - p1.getSecond()));

        for (PlayHistory history : historyList) {
            PQ.add(new Pair<>(history.musicID, history.totalPlayedTime));
        }

        Playlist playlist = new Playlist(
                "server",
                "TOP" + listSize + "랭킹",
                "TOP" + listSize + "랭킹 플레이리스트입니다.",
                new ArrayList<>());
        for (Pair<Integer, Long> p : PQ) {
            if (listSize-- == 0) break;
            playlist.musicList.add(p.getFirst());
        }

        return playlist;
    }
}
