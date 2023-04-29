package com.tftf.musictaggerserver.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tftf.util.Music;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


// todo : 파일에서 메타데이터 불러와 json으로 저장하는 코드 위치 물어볼 것
@RestController
public class MetadataController {
    final String metaPath = "src\\main\\resources\\metadata\\metaf.json";
    //todo : tika 라이브러리를 활용하여 음악파일에서 메타데이터 추출해보기

    @GetMapping("metadata")
    public @ResponseBody Music getMetadataById(@RequestParam("id") int id){

        try {
            FileReader reader = new FileReader(metaPath);
            JsonArray jsonArray = (JsonArray) JsonParser.parseReader(reader);
            reader.close();

            JsonObject obj = (JsonObject) jsonArray.get(id - 1000);

            return new Music(obj.get("id").getAsInt(), obj.get("title").getAsString(), obj.get("album").getAsString(), obj.get("artist").getAsString(),
                    obj.get("duration").getAsLong(), obj.get("path").getAsString(), obj.get("artUri").getAsString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

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
}
