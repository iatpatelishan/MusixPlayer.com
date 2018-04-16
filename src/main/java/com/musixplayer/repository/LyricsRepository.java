package com.musixplayer.repository;

import com.musixplayer.model.Lyrics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LyricsRepository extends JpaRepository<Lyrics, Long> {
}
