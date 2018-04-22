package com.musixplayer.service;

import com.musixplayer.model.Playlist;
import com.musixplayer.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public Playlist create(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public Optional<Playlist> findById(Long id) {
        return playlistRepository.findById(id);
    }

}