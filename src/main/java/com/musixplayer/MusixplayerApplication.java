package com.musixplayer;

import com.musixplayer.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableAutoConfiguration(exclude = { JacksonAutoConfiguration.class })
@EnableCaching
public class MusixplayerApplication {

    @Autowired
    static PersonRepository personRepository;


    public static void main(String[] args) {
        SpringApplication.run(MusixplayerApplication.class, args);
    }
}
