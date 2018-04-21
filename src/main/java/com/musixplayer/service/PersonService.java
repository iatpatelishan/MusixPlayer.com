package com.musixplayer.service;

import com.musixplayer.model.Person;
import com.musixplayer.repository.PersonRepository;
import com.musixplayer.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, RoleRepository roleRepository) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<Person> findByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    public Optional<Person> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public Person findByConfirmationToken(String confirmationToken) {
        return personRepository.findByConfirmationToken(confirmationToken);
    }

    public Person create(Person person){
        return personRepository.save(person);
    }


    public void deletePerson(String username){

        Person person = personRepository.findByUsername(username).orElse(null);

        if(person != null){
            personRepository.delete(person);
        }
    }

}
