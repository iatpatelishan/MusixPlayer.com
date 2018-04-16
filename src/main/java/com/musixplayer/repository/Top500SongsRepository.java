package com.musixplayer.repository;

import com.musixplayer.model.Song;
import com.musixplayer.model.Top500Songs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface Top500SongsRepository extends JpaRepository<Top500Songs, Long> {

    @Query("SELECT s FROM Top500Songs s WHERE s.country=:country")
    Optional<Top500Songs> findByCountry(@Param("country") String s);



}
