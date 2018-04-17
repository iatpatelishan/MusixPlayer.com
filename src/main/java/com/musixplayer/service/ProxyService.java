package com.musixplayer.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Service
public class ProxyService {

    @Cacheable("homepageSearch")
    public Object searchSongLastfm(String searchword, String searchtype) {
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


    @Cacheable("youtubeSearch")
    public Object searchSongYoutube(String searchword) {
        if(searchword!=null){
            String uri = "https://www.googleapis.com/youtube/v3/search?&maxResults=1&part=snippet&key=AIzaSyBvxuIQWp4wQkY0S9X80G9J9wmhVhYRWvg&q="+searchword+"%20song";
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(uri, String.class);
        }
        return new Object();
    }


    @Cacheable("lastfmMusicMbId")
    public Object searchSongInLastfmByMbId(String mbid) {
        if(mbid!=null){
            String uri = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=ecc8416e699592148ea368f3a381a819&format=json&mbid="+mbid;
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(uri, String.class);
        }
        return new Object();
    }

    @Cacheable("lastfmArtistDataMbId")
    public Object searchArtistDataInLastfmByMbId(String mbid) {
        if(mbid!=null){
            ///2.0/?method=artist.getinfo&artist=Cher&api_key=YOUR_API_KEY&format=json
            String uri = "http://ws.audioscrobbler.com/2.0/?method=artist.getInfo&api_key=ecc8416e699592148ea368f3a381a819&format=json&mbid="+mbid;
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(uri, String.class);
        }
        return new Object();
    }


}
