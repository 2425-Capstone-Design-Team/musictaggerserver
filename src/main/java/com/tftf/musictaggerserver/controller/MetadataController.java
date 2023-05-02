package com.tftf.musictaggerserver.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tftf.util.Music;
import com.tftf.util.Music;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
    @RequestMapping("/readmeta")
    public String readmeta() throws IOException {

        String DATA_DIRECTORY = "src\\main\\resources\\media\\";
        File dir = new File(DATA_DIRECTORY);
        String[] filenames = dir.list((f,name)->name.endsWith(".mp3"));
        String result="";
        for (String filename : filenames) {
            System.out.println(filename);
            result=result+filename;
        }
        File metadata = new File("src\\main\\resources\\metadata\\meta.txt");
        if(metadata.exists()){
            try {
                BufferedReader reader = new BufferedReader(
                        new FileReader("src\\main\\resources\\metadata\\meta.txt")                );
                String str;
                while ((str = reader.readLine()) != null) {
                    System.out.println(str);
                }
                reader.close();
                if (!(result==str)){
                    Path metafile = Paths.get("src\\main\\resources\\metadata\\meta.txt");
                    Files.deleteIfExists(metafile);
                    try{
                        BufferedWriter writer = new BufferedWriter(new FileWriter("src\\main\\resources\\metadata\\meta.txt",true));
                        writer.write(result);
                        writer.flush();
                        writer.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    Path metajsonfile = Paths.get("src\\main\\resources\\metadata\\meta.json");
                    Files.deleteIfExists(metajsonfile);
                    JSONObject listmp3meta = new JSONObject();
                    JSONArray meta_array = new JSONArray();
                    for(String filename: filenames){
                        meta_array.add(getMeta(filename));
                    }
                    listmp3meta.put("list_array",meta_array);
                    try {
                        FileWriter file = new FileWriter("src\\main\\resources\\metadata\\meta.json");
                        file.write(listmp3meta.toJSONString());
                        file.flush();
                        file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            Path metafile = Paths.get("src\\main\\resources\\metadata\\meta.txt");
            Files.deleteIfExists(metafile);
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter("src\\main\\resources\\metadata\\meta.txt",true));
                writer.write(result);
                writer.flush();
                writer.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            JSONObject listmp3meta = new JSONObject();
            JSONArray meta_array = new JSONArray();
            for(String filename: filenames){
                meta_array.add(getMeta(filename));
            }
            listmp3meta.put("list_array",meta_array);
            try {
                FileWriter file = new FileWriter("src\\main\\resources\\metadata\\meta.json");
                file.write(listmp3meta.toJSONString());
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }





        File file = new File("src\\main\\resources\\media\\1000.mp3");
        AudioFile f;

        {
            try {
                f = AudioFileIO.read(file);
            } catch (CannotReadException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TagException e) {
                throw new RuntimeException(e);
            } catch (ReadOnlyFileException e) {
                throw new RuntimeException(e);
            } catch (InvalidAudioFrameException e) {
                throw new RuntimeException(e);
            }
        }

        Tag tag = f.getTag();
        AudioHeader audioHeader = f.getAudioHeader();
        return tag.getFirst(FieldKey.ARTIST)+" "+tag.getFirst(FieldKey.ALBUM)+" "+tag.getFirst(FieldKey.TITLE)+" "+audioHeader.getTrackLength()+ tag.getFirst(FieldKey.COVER_ART);
    }
    public JSONObject getMeta(String filename)
    {
        JSONObject mp3meta = new JSONObject();

        File file = new File("src\\main\\resources\\media\\"+ filename);
        AudioFile f;

        {
            try {
                f = AudioFileIO.read(file);
            } catch (CannotReadException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TagException e) {
                throw new RuntimeException(e);
            } catch (ReadOnlyFileException e) {
                throw new RuntimeException(e);
            } catch (InvalidAudioFrameException e) {
                throw new RuntimeException(e);
            }
        }

        Tag tag = f.getTag();
        AudioHeader audioHeader = f.getAudioHeader();
        mp3meta.put("id",filename.substring(0,4));
        mp3meta.put("title",tag.getFirst(FieldKey.TITLE));
        mp3meta.put("album",tag.getFirst(FieldKey.ALBUM));
        mp3meta.put("artist",tag.getFirst(FieldKey.ARTIST));
        mp3meta.put("duration",audioHeader.getTrackLength()*1000);
        mp3meta.put("path",filename);
        mp3meta.put("artUri","artImg_"+filename.substring(4,4)+".jpg");
        return mp3meta;
    }

}
