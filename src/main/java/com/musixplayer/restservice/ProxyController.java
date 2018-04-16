package com.musixplayer.restservice;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
public class ProxyController {

    @GetMapping("/api/search")
    @Cacheable("books")
    public Object searchSongLastfm( @RequestParam(name="search", required=false) String searchword, @RequestParam(name="type", required=false) String searchtype) {
        String result="";
        if(searchtype!=null && searchword!=null){
            String url;
            if(searchtype.equals("song")){
                url = "http://ws.audioscrobbler.com/2.0/?method=track.search&api_key=ecc8416e699592148ea368f3a381a819&format=json&limit=20&track="+searchword;
            } else {
                url = "http://ws.audioscrobbler.com/2.0/?method=artist.search&api_key=ecc8416e699592148ea368f3a381a819&format=json&limit=20&artist="+searchword;
            }
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(url, Object.class);
        }
        return new Object();
    }

}
