package com.tftf.musictaggerserver.controller;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
public class ImageController {
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @GetMapping(
            value = "/img",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImageWithMediaType(@RequestParam("id") int id) {
        File file = new File("src\\main\\resources\\media\\"+ id + ".mp3");
        AudioFile audioFile;

        try {
            audioFile = AudioFileIO.read(file);
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            throw new RuntimeException(e);
        }

        Tag tag = audioFile.getTag();

        logger.info("getting music image from " + id);

        return tag.getFirstArtwork().getBinaryData();
    }
}
