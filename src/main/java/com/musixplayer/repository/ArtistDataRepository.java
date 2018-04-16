package com.musixplayer.repository;

import com.musixplayer.model.ArtistData;
import com.musixplayer.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistDataRepository extends JpaRepository<ArtistData, Long> {


    @Query("SELECT u FROM ArtistData u WHERE u.mbId=:mbid")
    Optional<ArtistData> findByMbId(@Param("mbid") String u);

}
