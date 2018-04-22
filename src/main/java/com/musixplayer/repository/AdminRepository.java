package com.musixplayer.repository;

import com.musixplayer.model.Admin;
import com.musixplayer.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long>{

    @Query("SELECT u FROM Admin u WHERE u.username =:username")
    Optional<Admin> findByUsername(@Param("username") String username);

    Admin findByConfirmationToken(String confirmationToken);

    @Query("SELECT u FROM Admin u WHERE u.email =:email")
    Optional<Admin> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM Admin u WHERE u.username=:username AND u.password=:password")
    Optional<Admin> findByCredentials(@Param("username") String username, @Param("password") String password);

}