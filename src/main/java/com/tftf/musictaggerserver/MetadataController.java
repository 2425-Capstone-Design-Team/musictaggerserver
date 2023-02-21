package com.tftf.musictaggerserver;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;

@RestController
public class MetadataController {
    final String metaPath = "src\\main\\resources\\meta\\metaf.json";

    @GetMapping("/metadata")
    public metaDTO getMeta(@RequestParam String code){

        JSONParser parser = new JSONParser();

        try {
            FileReader reader = new FileReader(metaPath);
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;

            reader.close();
            JSONObject meta = (JSONObject) jsonObject.get(code);
            String title = (String) meta.get("title");
            String singer = (String) meta.get("singer");
            String year = (String) meta.get("year");


            metaDTO result = new metaDTO(title,singer,year);
            return result;

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
      return null;
    }

}
