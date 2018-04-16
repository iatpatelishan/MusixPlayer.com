package com.musixplayer.controller;

import com.musixplayer.model.Person;
import com.musixplayer.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private SongService songService;


    @Autowired
    public HomeController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/")
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, Person person){
        modelAndView.addObject("user", person);
        modelAndView.addObject("listOfTop500Songs", songService.findAll());
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping("/person")
    public String person() {
        return "/person";
    }

    @GetMapping("/about")
    public String about() {
        return "/about";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

}