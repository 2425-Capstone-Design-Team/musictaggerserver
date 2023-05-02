package com.tftf.musictaggerserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.tftf.musictaggerserver.MetadataManager.initMetadataJson;

@SpringBootApplication
public class MusictaggerserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusictaggerserverApplication.class, args);

		initMetadataJson();
	}
}
