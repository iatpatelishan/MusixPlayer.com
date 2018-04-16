package com.musixplayer.repository;

import com.musixplayer.model.Editor;
import com.musixplayer.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EditorRepository extends JpaRepository<Editor, Long> {

    @Query("SELECT u FROM Editor u WHERE u.username =:username")
    Optional<Editor> findByUsername(@Param("username") String username);

    Editor findByConfirmationToken(String confirmationToken);

    @Query("SELECT u FROM Editor u WHERE u.email =:email")
    Optional<Editor> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM Editor u WHERE u.username=:username AND u.password=:password")
    Optional<Editor> findByCredentials(@Param("username") String username, @Param("password") String password);

}
