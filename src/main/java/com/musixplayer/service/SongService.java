package com.musixplayer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musixplayer.model.*;
import com.musixplayer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@Service
public class SongService {

    private final SongRepository songRepository;
    private final ArtistDataService artistDataService;
    private final LyricsService lyricsService;
    private final ProxyService proxyService;


    @Autowired
    public SongService(SongRepository songRepository, ArtistDataService artistDataService, LyricsService lyricsService, ProxyService proxyService) {
        this.songRepository = songRepository;
        this.artistDataService = artistDataService;
        this.lyricsService = lyricsService;
        this.proxyService = proxyService;
    }


    public Song create(Song song) {
        String newMbid = song.getMbId();
        Optional<Song> exisitingSong = findSongByMbid(newMbid);
        if (!exisitingSong.isPresent()) {
            return songRepository.save(song);
        } else {
            return exisitingSong.get();
        }
    }

    public Collection<Song> findAll() {
        return songRepository.findAll();
    }

    public Optional<Song> findSongByMbid(String mbid) {
        return songRepository.findByMbId(mbid);
    }

    public Song fetchSongandAdd(String mbid) {
        if(mbid.length()!=36){
            return null;
        }
        // if exists, then return existing
        Optional<Song> checksong = songRepository.findByMbId(mbid);
        if (checksong.isPresent()) {
            return checksong.get();
        }

        String result = (String) proxyService.searchSongInLastfmByMbId(mbid);
        Song song = new Song();
        try {
            JsonNode jsonsong = new ObjectMapper().readTree(result);
            if(jsonsong.has("error")){
                return null;
            }
            jsonsong=jsonsong.get("track");

            String artistmbId = jsonsong.get("artist").get("mbid").textValue();
            if(artistmbId.length()<20){
                return null;
            }
            ArtistData artistData = artistDataService.fetchArtistDataandAdd(artistmbId);
            if(artistData==null){
                return null;
            }

            String songname = jsonsong.get("name").textValue();
            Integer duration = Integer.parseInt(jsonsong.get("duration").textValue());
            String lastFmUrl = jsonsong.get("url").textValue();
            String imageUrl = "https://i.imgur.com/CM5NHYx.jpg";
            if(jsonsong.has("album") && jsonsong.get("album").has("image")){
                Iterator<JsonNode> images = jsonsong.get("album").get("image").elements();
                while (images.hasNext()) {
                    JsonNode imagenode = images.next();
                    imageUrl = imagenode.get("#text").textValue();
                }
            }
            if(imageUrl.length()<5){
               imageUrl = "https://i.imgur.com/CM5NHYx.jpg";
            }

            String description = "";
            if (jsonsong.has("wiki") && jsonsong.get("wiki").has("summary")) {
                description = jsonsong.get("wiki").get("summary").textValue();
            }
            String youtubeUrl = proxyService.searchSongYoutubeURL(artistData.getName()+" "+songname);

            Collection<ArtistData> artists = new ArrayList<ArtistData>();
            artists.add(artistData);

            song = new Song(mbid, songname, description, duration, lastFmUrl, imageUrl, youtubeUrl, artists);
            song = create(song);

            Collection<Song> songs = artistData.getSongs();
            if (songs == null) {
                songs = new ArrayList<Song>();
            }
            if (!songs.contains(song)) {
                songs.add(song);
                artistData.setSongs(songs);
                artistData = artistDataService.create(artistData);
            }

            Lyrics apiSeedsLyrics  = lyricsService.fetchApiSeedsLyricsAndAdd(song);
            if(apiSeedsLyrics != null){
                Collection<Lyrics> lyricslist = song.getLyrics();
                if(lyricslist==null){
                    lyricslist = new ArrayList<Lyrics>();
                }
                if (!lyricslist.contains(apiSeedsLyrics)) {
                    lyricslist.add(apiSeedsLyrics);
                    song.setLyrics(lyricslist);
                    song = create(song);
                }
            }

            return song;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Song updateSongViews(int views, Long songId){

        if(songRepository.findById(songId).isPresent()){

            Song song = songRepository.findById(songId).get();
            song.setViews(views);
            songRepository.save(song);
            return song;

        }

        else return  null;

    }

    public void deleteSongByMbid(String mbid){

        Song song = songRepository.findByMbId(mbid).orElse(null);

        if(song != null){

            songRepository.delete(song);
        }
    }

}
