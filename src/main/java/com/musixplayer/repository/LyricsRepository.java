package com.musixplayer.repository;

import com.musixplayer.model.Lyrics;
import com.musixplayer.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LyricsRepository extends JpaRepository<Lyrics, Long> {

    @Query("SELECT s FROM Lyrics s WHERE s.song=:song AND s.source=:source")
    Optional<Lyrics> findBySongAndSource(@Param("song") Song song, @Param("source") String source);
}
