package com.musixplayer.service;

import com.musixplayer.model.ArtistData;
import com.musixplayer.model.Person;
import com.musixplayer.repository.ArtistDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArtistDataService {

    private final ArtistDataRepository artistDataRepository;

    @Autowired
    public ArtistDataService(ArtistDataRepository artistDataRepository) {
        this.artistDataRepository = artistDataRepository;
    }

    public Optional<ArtistData> findByMbid(String mbid) {
        return artistDataRepository.findByMbId(mbid);
    }

    public ArtistData create(ArtistData artistData){
        return artistDataRepository.save(artistData);
    }

    public Optional<ArtistData> findArtistByMbid(String mbid) {
        return artistDataRepository.findByMbId(mbid);
    }


    public ArtistData fetchArtistDataandAdd(ArtistData artistData) {

        String mbid = artistData.getMbId();
        Optional<ArtistData> checkartistdata = artistDataRepository.findByMbId(mbid);
        if(checkartistdata.isPresent()){
            return checkartistdata.get();
        } else{
            artistData = artistDataRepository.save(artistData);
            return artistData;
        }
    }

}
