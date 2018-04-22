package com.musixplayer.service;

import com.musixplayer.model.Admin;
import com.musixplayer.model.Artist;
import com.musixplayer.model.ArtistData;
import com.musixplayer.model.Person;
import com.musixplayer.repository.ArtistRepository;
import com.musixplayer.repository.PersonRepository;
import com.musixplayer.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository, RoleRepository roleRepository) {
        this.artistRepository = artistRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<Artist> findByUsername(String username) {
        return artistRepository.findByUsername(username);
    }

    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    public Optional<Artist> findByEmail(String email) {
        return artistRepository.findByEmail(email);
    }

    public Artist findByConfirmationToken(String confirmationToken) {
        return artistRepository.findByConfirmationToken(confirmationToken);
    }

    public Artist create(Artist artist) {
        return artistRepository.save(artist);
    }

    public Optional<Artist> findArtistByArtistData(ArtistData artistData){
        return artistRepository.findByArtistDataId(artistData);
    }

}