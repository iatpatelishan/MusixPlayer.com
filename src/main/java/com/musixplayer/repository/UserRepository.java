package com.musixplayer.repository;

import com.musixplayer.model.Person;
import com.musixplayer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username =:username")
    Optional<User> findByUsername(@Param("username") String username);

    User findByConfirmationToken(String confirmationToken);

    @Query("SELECT u FROM User u WHERE u.email =:email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.username=:username AND u.password=:password")
    Optional<User> findByCredentials(@Param("username") String username, @Param("password") String password);
}
