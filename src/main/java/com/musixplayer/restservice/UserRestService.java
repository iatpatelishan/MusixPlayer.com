/*
package com.musixplayer.restservice;

import com.musixplayer.model.Person;
import com.musixplayer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserRestService {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/api/user")
    public List<Person> findAllUsers() {
        return (List<Person>) userRepository.findAll();
    }

}
*/
