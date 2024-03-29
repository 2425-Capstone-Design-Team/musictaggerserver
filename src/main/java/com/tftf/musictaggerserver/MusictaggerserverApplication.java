package com.tftf.musictaggerserver;

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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class MusictaggerserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusictaggerserverApplication.class, args);

		String DATA_DIRECTORY = "C:\\Users\\sskim\\Spring_Projects\\musictaggerserver\\src\\main\\resources\\media\\";
		File dir = new File(DATA_DIRECTORY);
		String[] filenames = dir.list((f,name)->name.endsWith(".mp3"));
		String result="";
		for (String filename : filenames) {
			System.out.println(filename);
			result=result+filename;
		}
		File metadata = new File("C:\\Users\\sskim\\Spring_Projects\\musictaggerserver\\src\\main\\resources\\media\\meta\\meta.txt");
		if(metadata.exists()){
			try {
				BufferedReader reader = new BufferedReader(
						new FileReader("C:\\Users\\sskim\\Spring_Projects\\musictaggerserver\\src\\main\\resources\\media\\meta\\meta.txt")                );
				String str;
				while ((str = reader.readLine()) != null) {
					System.out.println(str);
				}
				reader.close();
				if (!(result==str)){
					Path metafile = Paths.get("C:\\Users\\sskim\\Spring_Projects\\musictaggerserver\\src\\main\\resources\\media\\meta\\meta.txt");
					Files.deleteIfExists(metafile);
					try{
						BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\sskim\\Spring_Projects\\musictaggerserver\\src\\main\\resources\\media\\meta\\meta.txt",true));
						writer.write(result);
						writer.flush();
						writer.close();
					}catch (IOException e){
						e.printStackTrace();
					}
					//todo json 업데이트
					Path metajsonfile = Paths.get("C:\\Users\\sskim\\Spring_Projects\\musictaggerserver\\src\\main\\resources\\media\\meta\\meta.json");
					Files.deleteIfExists(metajsonfile);
					JSONObject listmp3meta = new JSONObject();
					JSONArray meta_array = new JSONArray();
					for(String filename: filenames){
						meta_array.add(getMeta(filename));
					}
					listmp3meta.put("list_array",meta_array);
					try {
						FileWriter file = new FileWriter("C:\\Users\\sskim\\Spring_Projects\\musictaggerserver\\src\\main\\resources\\media\\meta\\meta.json");
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
			Path metafile = Paths.get("C:\\Users\\sskim\\Spring_Projects\\musictaggerserver\\src\\main\\resources\\media\\meta\\meta.txt");
			try {
				Files.deleteIfExists(metafile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			try{
				BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\sskim\\Spring_Projects\\musictaggerserver\\src\\main\\resources\\media\\meta\\meta.txt",true));
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
				FileWriter file = new FileWriter("C:\\Users\\sskim\\Spring_Projects\\musictaggerserver\\src\\main\\resources\\media\\meta\\meta.json");
				file.write(listmp3meta.toJSONString());
				file.flush();
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static JSONObject getMeta(String filename)
	{
		JSONObject mp3meta = new JSONObject();

		File file = new File("C:\\Users\\sskim\\Spring_Projects\\musictaggerserver\\src\\main\\resources\\media\\"+ filename);
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
		mp3meta.put("Artist",tag.getFirst(FieldKey.ARTIST));
		mp3meta.put("Album",tag.getFirst(FieldKey.ALBUM));
		mp3meta.put("Title",tag.getFirst(FieldKey.TITLE));
		mp3meta.put("TrackLength",audioHeader.getTrackLength());

		return mp3meta;
	}

}
