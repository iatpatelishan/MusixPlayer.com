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
public class SongService {

    private final SongRepository songRepository;
    private final ArtistDataRepository artistDataRepository;
    private final ArtistDataService artistDataService;


    @Autowired
    public SongService(SongRepository songRepository,ArtistDataService artistDataService,ArtistDataRepository artistDataRepository) {
        this.songRepository = songRepository;
        this.artistDataService = artistDataService;
        this.artistDataRepository = artistDataRepository;
    }


    public Song create(Song song){

        String newMbid = song.getMbId();
        Optional<Song> exisitingSong = findSongByMbid(newMbid);

        if(!exisitingSong.isPresent()){

            return songRepository.save(song);
        }
        else
            return exisitingSong.get();
    }

    public Collection<Song> findAll(){
        return songRepository.findAll();
    }

    public Optional<Song> findSongByMbid(String mbid){
        return songRepository.findByMbId(mbid);
    }

    public Song fetchSongandAdd(String mbid){

        Optional<Song>  checksong = songRepository.findByMbId(mbid);
        if(checksong.isPresent()){
            return checksong.get();
        }

        String uri = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=ecc8416e699592148ea368f3a381a819&mbid="+mbid+"&track=believe&format=json";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        Song song = new Song();

        try{
            JsonNode jsonsong = new ObjectMapper().readTree(result).get("track");

            String artistmbId = jsonsong.get("artist").get("mbid").textValue();
            String artistName = jsonsong.get("artist").get("name").textValue();
            ArtistData artistData = new ArtistData();
            artistData.setMbId(artistmbId);
            artistData.setName(artistName);
            artistData = artistDataService.fetchArtistDataandAdd(artistData);

            String songname = jsonsong.get("name").textValue();
            Integer duration = Integer.parseInt(jsonsong.get("duration").textValue());
            String lastFmUrl = jsonsong.get("url").textValue();
            Iterator<JsonNode> images = jsonsong.get("album").get("image").elements();
            String imageUrl=null;
            while(images.hasNext()) {
                JsonNode imagenode = images.next();
                imageUrl=imagenode.get("#text").textValue();
            }
            String description="";
            if(jsonsong.has("wiki") && jsonsong.get("wiki").has("summary")){
                description = jsonsong.get("wiki").get("summary").textValue();
            }

            Collection<ArtistData> artists = new ArrayList<ArtistData>();
            artists.add(artistData);

            song = new Song(mbid,songname,description,duration,lastFmUrl,imageUrl,artists);
            song = songRepository.save(song);

            Collection<Song> songs = artistData.getSongs();
            if(songs == null || (!songs.contains(song))){
                if(songs==null){
                    songs = new ArrayList<Song>();
                }
                songs.add(song);
                artistData.setSongs(songs);
                artistData=artistDataRepository.save(artistData);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return song;
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

}
