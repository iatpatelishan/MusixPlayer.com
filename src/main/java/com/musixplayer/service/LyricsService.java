package com.musixplayer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musixplayer.model.*;
import com.musixplayer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@Service
public class LyricsService {

    private final LyricsRepository lyricsRepository;
    private final ProxyService proxyService;


    @Autowired
    public LyricsService(LyricsRepository lyricsRepository, ProxyService proxyService) {
        this.lyricsRepository = lyricsRepository;
        this.proxyService = proxyService;
    }

    public Lyrics create(Lyrics lyrics) {
            return lyricsRepository.save(lyrics);
    }

    public Lyrics fetchApiSeedsLyricsAndAdd(Song song) {

        Optional<Lyrics> checkApiSeedsLyrics = lyricsRepository.findBySongAndSource(song,"ApiSeeds");
        if (!checkApiSeedsLyrics.isPresent()) {
            String ApiSeedsLyrics = (String) proxyService.searchLyricsInApiSeeds(song.getName(),song.getArtists().iterator().next().getName());
            if(ApiSeedsLyrics==null){
                return null;
            }
            Lyrics lyrics = new Lyrics();
            try {
                JsonNode jsonlyrics = new ObjectMapper().readTree(ApiSeedsLyrics);
                if(!jsonlyrics.has("error")){
                    String result = jsonlyrics.get("result").get("track").get("text").textValue();
                    lyrics.setLyrics(result);
                    lyrics.setSong(song);
                    lyrics.setSource("ApiSeeds");
                    lyrics=create(lyrics);
                    return lyrics;
                }
            } catch (IOException e) {
                return null;
            }
        }
            return null;

    }
}