package com.musixplayer.repository;


import com.musixplayer.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s FROM Song s WHERE s.mbId=:mbid")
    Optional<Song> findByMbId(@Param("mbid") String s);
}
