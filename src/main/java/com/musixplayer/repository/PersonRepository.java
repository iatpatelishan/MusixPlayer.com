package com.musixplayer.repository;

import com.musixplayer.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

   @Query("SELECT u FROM Person u WHERE u.username =:username")
    Optional<Person> findByUsername(@Param("username") String username);

    Person findByConfirmationToken(String confirmationToken);

    @Query("SELECT u FROM Person u WHERE u.email =:email")
    Optional<Person> findByEmail(@Param("email") String email);

   @Query("SELECT u FROM Person u WHERE u.username=:username AND u.password=:password")
    Optional<Person> findByCredentials(@Param("username") String username, @Param("password") String password);
}
