package com.musixplayer.service;

import com.musixplayer.model.Top500Songs;
import com.musixplayer.model.Song;
import com.musixplayer.repository.SongRepository;
import com.musixplayer.repository.Top500SongsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class Top500SongsService {

    private final Top500SongsRepository top500SongsRepository;
    private final SongRepository songRepository;

    @Autowired
    public Top500SongsService(Top500SongsRepository top500SongsRepository,SongRepository songRepository) {
        this.top500SongsRepository = top500SongsRepository;
        this.songRepository = songRepository;
    }

    public Top500Songs create(Song song, String country) {

        Optional<Top500Songs> existingList = top500SongsRepository.findByCountry(country);
        Top500Songs top500SongList;

        // check if the top 500 list exists of this country
        if(!existingList.isPresent()){
            top500SongList = new Top500Songs(country);
        }
        else{
            top500SongList = existingList.get();
        }

        // get the songs  of this list
        Collection<Song> existingSongList = top500SongList.getSongs();
        Song exisitingSong = songRepository.findByMbId(song.getMbId()).get();

        // check if this song exists in the list
        if(existingSongList == null){
            existingSongList.add(exisitingSong);
            top500SongList.setSongs(existingSongList);
            top500SongsRepository.save(top500SongList);
        }
        if (existingSongList!= null && !existingSongList.contains(exisitingSong)) {

                existingSongList.add(exisitingSong);
                top500SongList.setSongs(existingSongList);
                top500SongsRepository.save(top500SongList);
        }
        return top500SongList;
    }

    public void deleteAllByCountry(String country) {

        Optional<Top500Songs> top500SongList = top500SongsRepository.findByCountry(country);

        if(top500SongList.isPresent()) {
            top500SongsRepository.delete(top500SongList.get());
        }


    }


    public Top500Songs findTopSongsByCountry(String country){

        if(top500SongsRepository.findByCountry(country).isPresent())
             return top500SongsRepository.findByCountry(country).get();
        else
            return null;


    }



}
