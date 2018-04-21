package com.musixplayer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

@Service
public class ProxyService {

    @Cacheable("homepageSearch")
    public Object searchSongLastfm(String searchword, String searchtype) {
        if (searchtype != null && searchword != null) {
            String url;
            if (searchtype.equals("song")) {
                url = "http://ws.audioscrobbler.com/2.0/?method=track.search&api_key=ecc8416e699592148ea368f3a381a819&format=json&limit=20&track=" + searchword;
            } else {
                url = "http://ws.audioscrobbler.com/2.0/?method=artist.search&api_key=ecc8416e699592148ea368f3a381a819&format=json&limit=20&artist=" + searchword;
            }
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(url, Object.class);
        }
        return new Object();
    }


    @Cacheable("youtubeSearch")
    public String searchSongYoutubeURL(String searchword) {
        if (searchword != null) {
            String uri = "https://www.googleapis.com/youtube/v3/search?&maxResults=1&part=snippet&key=AIzaSyBvxuIQWp4wQkY0S9X80G9J9wmhVhYRWvg&q=" + searchword;
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);
            try {
                JsonNode jsonsong = new ObjectMapper().readTree(result);
                if(!jsonsong.has("items")){
                    return null;
                }
                jsonsong=jsonsong.get("items");
                Iterator<JsonNode> songnodeiterator = jsonsong.elements();
                if (songnodeiterator.hasNext()) {
                    JsonNode songnode = songnodeiterator.next();
                    if (songnode.has("id")){
                        return songnode.get("id").get("videoId").textValue();
                    }
                } else {
                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Cacheable("apiSeedsLyrics")
    public String searchLyricsInApiSeeds(String song, String artist) {
        String result=null;
        if (song != null && artist!=null) {
            String uri = "https://orion.apiseeds.com/api/music/lyric/"+artist.replaceAll("[^A-Za-z0-9 ]", "")+"/"+song.replaceAll("[^A-Za-z0-9 ]", "")+"?apikey=IbBAffPwx6dAOUHht2ktKhunfB29V6ZLc16v0izDTcmdJ7M5mtX2MRlwsoYyE8Va";

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set( "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            try {
                result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();
                return result;
            } catch (HttpClientErrorException ex){
                if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
                    //throw ex;
                }
            }


        }
        return result;
    }


    @Cacheable("lastfmMusicMbId")
    public Object searchSongInLastfmByMbId(String mbid) {
        if (mbid != null) {
            String uri = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=ecc8416e699592148ea368f3a381a819&format=json&mbid=" + mbid;
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(uri, String.class);
        }
        return new Object();
    }

    @Cacheable("lastfmArtistDataMbId")
    public Object searchArtistDataInLastfmByMbId(String mbid) {
        if (mbid != null) {
            ///2.0/?method=artist.getinfo&artist=Cher&api_key=YOUR_API_KEY&format=json
            String uri = "http://ws.audioscrobbler.com/2.0/?method=artist.getInfo&api_key=ecc8416e699592148ea368f3a381a819&format=json&mbid=" + mbid;
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(uri, String.class);
        }
        return new Object();
    }


}
