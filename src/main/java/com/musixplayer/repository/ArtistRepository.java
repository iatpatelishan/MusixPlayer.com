package com.musixplayer.repository;

import com.musixplayer.model.Artist;
import com.musixplayer.model.ArtistData;
import com.musixplayer.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    @Query("SELECT u FROM Artist u WHERE u.username =:username")
    Optional<Artist> findByUsername(@Param("username") String username);

    Artist findByConfirmationToken(String confirmationToken);

    @Query("SELECT u FROM Artist u WHERE u.email =:email")
    Optional<Artist> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM Artist u WHERE u.username=:username AND u.password=:password")
    Optional<Artist> findByCredentials(@Param("username") String username, @Param("password") String password);

    @Query("SELECT u FROM Artist u WHERE u.artistData=:artistData")
    Optional<Artist> findByArtistDataId(@Param("artistData") ArtistData artistData);
}
