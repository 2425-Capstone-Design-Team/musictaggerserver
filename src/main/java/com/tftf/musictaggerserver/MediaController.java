package com.tftf.musictaggerserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

@RestController
public class MediaController {
    final String mediaPath = "src\\main\\resources\\media\\";

    @GetMapping(value="/media")
    public void mp4Stream (HttpServletRequest request , HttpServletResponse response, @RequestParam("title") String mediaFileName) throws IOException {
        File file = new File(mediaPath + mediaFileName);
        RandomAccessFile randomFile = new RandomAccessFile(file, "r");
        long rangeStart; //요청 범위의 시작 위치
        long rangeEnd; //요청 범위의 끝 위치
        boolean isPart=false; //부분 요청일 경우 true, 전체 요청의 경우 false
        try{ //동영상 파일 크기
            long movieSize = randomFile.length(); //스트림 요청 범위, request의 헤더에서 range를 읽는다.
            String range = request.getHeader("range");
            if(range!=null) {
                if (range.endsWith("-")) {
                    range = range + (movieSize - 1);
                }
                int idxm = range.trim().indexOf("-");
                rangeStart = Long.parseLong(range.substring(6, idxm));
                rangeEnd = Long.parseLong(range.substring(idxm + 1));
                if (rangeStart > 0) {
                    isPart = true;
                }
            }
            else {
                rangeStart =0;
                rangeEnd = movieSize -1;
            }
            long partSize = rangeEnd - rangeStart + 1;
            response.reset();
            response.setStatus(isPart ? 206 : 200);

            if (file.getName().endsWith(".mp3"))
                response.setContentType("audio/mpeg");
            else if(file.getName().endsWith(".mp4"))
                response.setContentType("video/mp4");

            response.setHeader("Content-Range", "bytes "+rangeStart+"-"+rangeEnd+"/"+movieSize);
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", ""+partSize);
            OutputStream out = response.getOutputStream();
            randomFile.seek(rangeStart);
            int bufferSize = 8*1024;
            byte[] buf = new byte[bufferSize];
            do{
                int block = partSize > bufferSize ? bufferSize : (int)partSize;
                int len = randomFile.read(buf, 0, block);
                out.write(buf, 0, len);
                partSize -= block;
            }while(partSize > 0);
        } catch(IOException e){
        } finally{
            randomFile.close();
        }
    }
}