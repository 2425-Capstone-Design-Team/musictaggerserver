package com.tftf.musictaggerserver;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.io.IOException;

@SpringBootApplication
public class MusictaggerserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusictaggerserverApplication.class, args);
	}

}
