package com.tftf.musictaggerserver.controller;

import com.google.gson.JsonObject;
import com.tftf.musictaggerserver.db.PlaytimeHistoryDAO;
import com.tftf.util.MusicTag;
import com.tftf.util.PlayHistory;
import com.tftf.util.PlaytimeHistoryDTO;
import com.tftf.util.Surroundings;
import com.tftf.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static java.lang.CharSequence.compare;

@RestController
@RequestMapping(value="recommend")
public class RecommendationController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    PlaytimeHistoryDAO playtimeHistoryDAO;
    
    // todo : 태그정보 받아와서 추천리스트 만들고 반환해주기
    @PostMapping("personalized")
    public @ResponseBody List<Integer> getPersonalizedList(@RequestParam("email") String email,
                                                           @RequestBody Surroundings surroundings,
                                                           @RequestParam int listSize) {

        List<PlaytimeHistoryDTO> histories = playtimeHistoryDAO.select(email);

        // PriorityQueue<Pair<MusicID, Point>>, 내림차순
        PriorityQueue<Pair<Integer, Integer>> PQ = new PriorityQueue<>(10,
                (p1, p2) -> p2.getSecond() - p1.getSecond());

        for (PlaytimeHistoryDTO historyDto : histories) {
            PlayHistory history = new PlayHistory();
            history.importFromJson(historyDto.getHistoryJO());
            MusicTag historyMusicTag = history.getMusicTag();

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

    /*
    @GetMapping(value="metadatalist", params="ids")
    public @ResponseBody List<Music> getMetadataList(@RequestParam("ids") List<Integer> ids) {
        try {
            FileReader reader = new FileReader(metaPath);
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            reader.close();

            List<Music> ls = new ArrayList<>();

            for (int id : ids) {
                JsonObject obj = jsonArray.get(id - 1000).getAsJsonObject();
                ls.add(new Music(obj.get("id").getAsInt(), obj.get("title").getAsString(), obj.get("album").getAsString(), obj.get("artist").getAsString(),
                        obj.get("duration").getAsLong(), obj.get("path").getAsString(), obj.get("artUri").getAsString()));
            }

            return ls;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping(value="metadatalist", params = {"items", "name"})
    public @ResponseBody List<Music> getMetadataList(@RequestParam("items") List<String> items, @RequestParam("name") String name) {
        try {
            FileReader reader = new FileReader(metaPath);
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            reader.close();

            List<Music> ls = new ArrayList<>();

            for (String s : items) {
                for (Object o : jsonArray) {
                    JsonObject obj = (JsonObject) o;
                    if (obj.get(s).getAsString().equals(name)) {
                        ls.add(new Music(obj.get("id").getAsInt(), obj.get("title").getAsString(), obj.get("album").getAsString(), obj.get("artist").getAsString(),
                                obj.get("duration").getAsLong(), obj.get("path").getAsString(), obj.get("artUri").getAsString()));
                    }
                }
            }

            return ls;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    */
}
