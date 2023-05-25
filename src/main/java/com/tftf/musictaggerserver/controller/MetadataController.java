package com.tftf.musictaggerserver.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tftf.util.Music;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MetadataController {
    final String metaPath = "src\\main\\resources\\metadata\\meta.json";
    //todo : tika 라이브러리를 활용하여 음악파일에서 메타데이터 추출해보기

    @GetMapping("metadata")
    public @ResponseBody Music getMetadataById(@RequestParam("musicID") int musicID){

        try {
            FileReader reader = new FileReader(metaPath);
            JsonArray jsonArray = (JsonArray) JsonParser.parseReader(reader);
            reader.close();

            JsonObject obj = (JsonObject) jsonArray.get(musicID - 1000);

            return new Music(obj.get("id").getAsInt(), obj.get("title").getAsString(), obj.get("album").getAsString(), obj.get("artist").getAsString(),
                    obj.get("duration").getAsLong(), obj.get("path").getAsString(), obj.get("artUri").getAsString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping(value="metadatalist", params="musicIDList")
    public @ResponseBody List<Music> getMetadataList(@RequestParam("musicIDList") List<Integer> musicIDList) {
        try {
            FileReader reader = new FileReader(metaPath);
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            reader.close();

            List<Music> ls = new ArrayList<>();

            for (int id : musicIDList) {
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

    @GetMapping(value="metadatalist", params = {"criterion", "keyword"})
    public @ResponseBody List<Music> getMetadataList(@RequestParam("criterion") List<String> criterion, @RequestParam("keyword") String keyword) {
        try {
            FileReader reader = new FileReader(metaPath);
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            reader.close();

            List<Music> ls = new ArrayList<>();

            for (String s : criterion) {
                for (Object o : jsonArray) {
                    JsonObject obj = (JsonObject) o;
                    if (obj.get(s).getAsString().contains(keyword)) {
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
}
