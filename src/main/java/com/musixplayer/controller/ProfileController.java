package com.musixplayer.controller;


import com.musixplayer.model.Person;
import com.musixplayer.service.PersonService;
import com.musixplayer.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private PersonService personService;

    @Autowired
    public ProfileController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{username}")
    public ModelAndView getProfileDetails(ModelAndView modelAndView, @PathVariable("username") String username) {

        Person person = personService.findByUsername(username).orElse(null);
        modelAndView.addObject("person",person);
        modelAndView.setViewName("profile");
        return modelAndView;
    }

}
