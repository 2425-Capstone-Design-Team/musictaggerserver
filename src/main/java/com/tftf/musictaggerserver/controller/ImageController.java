package com.tftf.musictaggerserver.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class ImageController {
    final String basePath = "/artImg/";

    @GetMapping(
            value = "/img",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImageWithMediaType(@RequestParam("id") int id) throws IOException {
        InputStream in = getClass().getResourceAsStream(basePath + "artImg_" + id + ".jpg");
        return in.readAllBytes();
    }
}
