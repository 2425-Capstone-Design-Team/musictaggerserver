package com.tftf.musictaggerserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.util.Pair;
import java.util.*;

@RestController
@RequestMapping(value="recommend")
public class RecommendationController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    PlaytimeHistoryDAO playtimeHistoryDAO;

    public HashMap<CharSequence, CharSequence> getMusicTag(JsonObject musicHistoryJO) {

        HashMap<CharSequence, HashMap<CharSequence, Integer>> historySum = new HashMap<>();

        for (String category : musicHistoryJO.keySet()) {
            if (!historySum.containsKey(category)) {
                historySum.put(category, new HashMap<>());
            }

            JsonObject tagJO = musicHistoryJO.get(category).getAsJsonObject();
            for (String tagKey : tagJO.keySet()) {
                if (!historySum.get(category).containsKey(tagKey)) {
                    historySum.get(category).put(tagKey, 0);
                }

                int tagVal = tagJO.get(tagKey).getAsInt();
                historySum.get(category).replace(tagKey, historySum.get(category).get(tagKey) + tagVal);
            }
        }

        // HashMap<태그카테고리, (내림차순)PriorityQueue<Pair<점수, 주변정보>>>
        HashMap<CharSequence, PriorityQueue<Pair<CharSequence, Integer>>> tagRank = new HashMap<>() {{
            for (CharSequence category : historySum.keySet()) {
                PriorityQueue<Pair<CharSequence, Integer>> categoryRank = new PriorityQueue<>(historySum.get(category).size(), Comparator.comparingInt(Pair::getSecond));
                for (CharSequence tagKey : historySum.get(category).keySet()) {
                    int tagVal = historySum.get(category).get(tagKey);
                    categoryRank.add(Pair.of(tagKey, tagVal));
                }
                put(category, categoryRank);
            }
        }};

        HashMap<CharSequence, CharSequence> musicTag = new HashMap<>();

        for (CharSequence category : tagRank.keySet()) {
            if (!tagRank.get(category).isEmpty()) {
                musicTag.put(category, tagRank.get(category).peek().getFirst());
            }
        }

        return musicTag;
    }
    
    // todo : 태그정보 받아와서 추천리스트 만들고 반환해주기
    @GetMapping("personalized")
//    public @ResponseBody List<Integer> getPersonalizedList(@RequestParam("email") String email, @RequestBody JsonObject surroundings) {
    public @ResponseBody List<Integer> getPersonalizedList(@RequestParam("email") String email) {


        HashMap<Integer, JsonObject> history = playtimeHistoryDAO.select(email);

        for (int musicId : history.keySet()) {
            HashMap<CharSequence, CharSequence> musicTag = getMusicTag(history.get(musicId));

            log.info("musicTag : {}", musicTag);
        }
        
        // todo : getMusicTag 테스트 필요

        // todo : 현재 주변 환경을 나타내는 클래스 필요?, 매개변수로 클라이언트의 주변환경 정보를 받아와야함

        /*
        String currentWeather = surroundings.get("날씨").getAsString();
        String currentTime = surroundings.get("시간").getAsString();
        String currentSeason = surroundings.get("계절").getAsString();
        */

        return new ArrayList<>();
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
