package com.tftf.musictaggerserver;

import com.mysql.cj.xdevapi.JsonArray;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MetadataController {
    final String metaPath = "src\\main\\resources\\metadata\\metaf.json";
    //todo : tika 라이브러리를 활용하여 음악파일에서 메타데이터 추출해보기

    @GetMapping("metadata")
    public @ResponseBody Music getMetadataById(@RequestParam("id") int id){

        JSONParser parser = new JSONParser();

        try {
            FileReader reader = new FileReader(metaPath);
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            reader.close();

            JSONObject obj = (JSONObject) jsonArray.get(id - 1000);

            return new Music(((Long) obj.get("id")).intValue(), obj.get("title").toString(), obj.get("album").toString(), obj.get("artist").toString(),
                    (Long) obj.get("duration"), obj.get("path").toString(), obj.get("artUri").toString());

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("metadatalist")
    public @ResponseBody List<Music> getMetadataList(@RequestParam("items") List<String> items, @RequestParam("name") String name){

        JSONParser parser = new JSONParser();

        try {
            FileReader reader = new FileReader(metaPath);
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            reader.close();

            List<Music> ls = new ArrayList<>();

            for (String s : items) {
                for (Object o : jsonArray) {
                    JSONObject obj = (JSONObject) o;
                    if (obj.get(s).equals(name)) {
                        ls.add(new Music(((Long) obj.get("id")).intValue(), obj.get("title").toString(), obj.get("album").toString(), obj.get("artist").toString(),
                                (Long) obj.get("duration"), obj.get("path").toString(), obj.get("artUri").toString()));
                    }
                }
            }

            return ls;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
