package com.tftf.musictaggerserver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;

@RestController


public class MetadataController {
    final String metaPath = "src\\main\\resources\\meta\\meta.json";

    @GetMapping(value = "/meta")
    @ResponseBody
    public metaDTO getMeta(@RequestParam int code){

        JSONParser parser = new JSONParser();
        metaDTO result = null;
        try {
            FileReader reader = new FileReader(metaPath);
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;

            reader.close();
            JSONObject meta = (JSONObject) jsonObject.get(code);
            String title = (String) meta.get("title");
            String singer = (String) meta.get("singer");
            String year = (String) meta.get("year");


            result = new metaDTO(title,singer,year);


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

}
