package com.tftf.musictaggerserver.controller;

import com.tftf.musictaggerserver.db.PlayHistoryDAO;
import com.tftf.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping(value="recommend")
public class RecommendationController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    PlayHistoryDAO playHistoryDAO;
    
    // todo : 태그정보 받아와서 추천리스트 만들고 반환해주기
    @PostMapping("personalized")
    public @ResponseBody Playlist getPersonalizedList(@RequestParam String userID,
                                                           @RequestBody Surroundings surroundings,
                                                           @RequestParam int listSize) {

        List<PlayHistory> historyList = playHistoryDAO.select(userID);

        // PriorityQueue<Pair<MusicID, Point>>, 내림차순
        PriorityQueue<Pair<Integer, Integer>> PQ = new PriorityQueue<>(listSize,
                (p1, p2) -> p2.getSecond() - p1.getSecond());

        for (PlayHistory history : historyList) {
            MusicTag historyMusicTag = MusicTagger.getMusicTag(history);

            int score = 0;
            for (String category : surroundings.infoMap.keySet()) {
                String surroundingsInfo = surroundings.infoMap.get(category);
                String historyMusicTagInfo = historyMusicTag.tagMap.get(category);

                if (historyMusicTagInfo.equals(surroundingsInfo)) {
                    score++;
                }
            }

            PQ.add(new Pair<>(history.musicID, score));
        }

        Playlist playlist = new Playlist(
                "server",
                "개인 맞춤 추천 리스트",
                "당신의 음악 취향에 기반하여 추천된 플레이리스트입니다.",
                new ArrayList<>());

        for (Pair<Integer, Integer> p : PQ) {
            if (listSize-- == 0) break;
            playlist.musicIDList.add(p.getFirst());
        }

        return playlist;
    }

    @PostMapping("generalized")
    public @ResponseBody Playlist getGeneralizedList(@RequestBody Surroundings surroundings,
                                                           @RequestParam int listSize) {

        List<PlayHistory> historyList = playHistoryDAO.selectAll();

        // PriorityQueue<Pair<MusicID, Point>>, 내림차순
        PriorityQueue<Pair<Integer, Integer>> PQ = new PriorityQueue<>(listSize,
                (p1, p2) -> p2.getSecond() - p1.getSecond());

        for (PlayHistory history : historyList) {
            MusicTag historyMusicTag = MusicTagger.getMusicTag(history);

            int score = 0;
            for (String category : surroundings.infoMap.keySet()) {
                String surroundingsInfo = surroundings.infoMap.get(category);
                String historyMusicTagInfo = historyMusicTag.tagMap.get(category);

                if (historyMusicTagInfo.equals(surroundingsInfo)) {
                    score++;
                }
            }

            PQ.add(new Pair<>(history.musicID, score));
        }

        Playlist playlist = new Playlist(
                "server",
                "보편 맞춤 추천 리스트",
                "사용자들의 보편적인 음악 취향에 기반하여 추천된 플레이리스트입니다.",
                new ArrayList<>());

        for (Pair<Integer, Integer> p : PQ) {
            if (listSize-- == 0) break;
            playlist.musicIDList.add(p.getFirst());
        }

        return playlist;
    }

    @PostMapping("theme")
    public @ResponseBody List<Playlist> getThemeList(@RequestBody Surroundings surroundings,
                                               @RequestParam int listSize) {

        List<Playlist> ThemePlaylist = new ArrayList<>();
        List<PlayHistory> historyList = playHistoryDAO.selectAll();

        for (String category : surroundings.infoMap.keySet()) {
            String surroundingsInfo = surroundings.infoMap.get(category);

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
                playlist.musicIDList.add(p.getFirst());
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
            playlist.musicIDList.add(p.getFirst());
        }

        return playlist;
    }
}
