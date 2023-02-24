package com.tftf.musictaggerserver;

import com.mysql.cj.xdevapi.JsonArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;

@RestController
public class MetadataController {
    final String metaPath = "src\\main\\resources\\metadata\\metaf.json";

    @GetMapping("/metadata")
    public @ResponseBody Music getMetadata(@RequestParam("title") String title){

        JSONParser parser = new JSONParser();

        try {
            FileReader reader = new FileReader(metaPath);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            reader.close();

            JSONObject metaObj = (JSONObject) jsonObject.get(title);

            return new Music(title, metaObj.get("album").toString(), metaObj.get("artist").toString(),
                    (Long)metaObj.get("duration"), metaObj.get("path").toString(), metaObj.get("artUri").toString());

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
