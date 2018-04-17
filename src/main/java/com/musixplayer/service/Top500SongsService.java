package com.musixplayer.service;

import com.musixplayer.model.Top500Songs;
import com.musixplayer.model.Song;
import com.musixplayer.repository.SongRepository;
import com.musixplayer.repository.Top500SongsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class Top500SongsService {

    private final Top500SongsRepository top500SongsRepository;
    private final SongRepository songRepository;

    @Autowired
    public Top500SongsService(Top500SongsRepository top500SongsRepository, SongRepository songRepository) {
        this.top500SongsRepository = top500SongsRepository;
        this.songRepository = songRepository;
    }

    public Top500Songs create(Song song, String country) {
        Top500Songs top500Songs = top500SongsRepository.findByCountry(country).orElse(null);
        if (top500Songs==null) {
            top500Songs = new Top500Songs(country);
        }

        // get the songs  of this list
        Collection<Song> existingSongList = top500Songs.getSongs();
        if (existingSongList == null) {
            existingSongList = new ArrayList<Song>();
        }

        Song exisitingSong = songRepository.findByMbId(song.getMbId()).get();
        if (!existingSongList.contains(exisitingSong)) {
            existingSongList.add(exisitingSong);
            top500Songs.setSongs(existingSongList);
            top500Songs = top500SongsRepository.save(top500Songs);
        }
        return top500Songs;
    }

    public void emptyAllSongsByCountry(String country) {
        Top500Songs top500Song = top500SongsRepository.findByCountry(country).orElse(null);
        if (top500Song!=null) {
            top500Song.setSongs(new ArrayList<Song>());
            top500SongsRepository.save(top500Song);
        }
    }


    public Top500Songs findTopSongsByCountry(String country) {

        return top500SongsRepository.findByCountry(country).get();

    }


}
