package com.musixplayer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/song")
public class SongController {

    @GetMapping("/{mbid}")
    public ModelAndView getSong(ModelAndView modelAndView, @PathVariable("userId") String mbId) {
        modelAndView.setViewName("song");
        return modelAndView;
    }
}
