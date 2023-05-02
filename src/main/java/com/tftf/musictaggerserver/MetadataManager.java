package com.tftf.musictaggerserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MetadataManager {

    private static final Logger logger = LoggerFactory.getLogger(MusictaggerserverApplication.class);

    public static void initMetadataJson() {
        String DATA_DIRECTORY = "src\\main\\resources\\media\\";
        File dir = new File(DATA_DIRECTORY);
        String[] filenames = dir.list((f,name)->name.endsWith(".mp3"));

        try {
            Path metajsonfile = Paths.get("src\\main\\resources\\metadata\\meta.json");
            Files.deleteIfExists(metajsonfile);

            JsonArray meta_array = new JsonArray();

            for(String filename: filenames){
                JsonObject musicMetadata = getMusicMetadata(filename);
                if (musicMetadata != null) {
                    meta_array.add(musicMetadata);
                }
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            try {
                FileWriter file = new FileWriter("src\\main\\resources\\metadata\\meta.json");
                file.write(gson.toJson(meta_array));
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonObject getMusicMetadata(String filename)
    {
        JsonObject mp3meta = new JsonObject();

        File file = new File("src\\main\\resources\\media\\"+ filename);
        AudioFile audioFile;

        try {
            audioFile = AudioFileIO.read(file);
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            throw new RuntimeException(e);
        }

        Tag tag = audioFile.getTag();
        AudioHeader audioHeader = audioFile.getAudioHeader();

        String idStr = filename.substring(0, 4);
        int musicID;

        try {
            musicID = Integer.parseInt(idStr);
            logger.info("getting music metadata from " + filename);
        } catch (NumberFormatException e) {
            logger.warn("music file name must be an Integer -> " + filename);
            return null;
        }

        mp3meta.addProperty("id", musicID);
        mp3meta.addProperty("title",tag.getFirst(FieldKey.TITLE));
        mp3meta.addProperty("album",tag.getFirst(FieldKey.ALBUM));
        mp3meta.addProperty("artist",tag.getFirst(FieldKey.ARTIST));
        mp3meta.addProperty("duration",audioHeader.getTrackLength()*1000);
        mp3meta.addProperty("path",filename);
        mp3meta.addProperty("artUri","artImg_"+filename.substring(0, 4)+".jpg");

        return mp3meta;
    }
}
