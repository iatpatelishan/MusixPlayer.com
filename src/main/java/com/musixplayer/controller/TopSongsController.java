package com.musixplayer.controller;

import com.musixplayer.model.Person;
import com.musixplayer.model.Top500Songs;
import com.musixplayer.service.SongService;
import com.musixplayer.service.Top500SongsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
@RequestMapping("/top")
public class TopSongsController {

    private SongService songService;
    private Top500SongsService top500SongsService;

    @Autowired
    public TopSongsController(SongService songService, Top500SongsService top500SongsService){

        this.songService = songService;
        this.top500SongsService = top500SongsService;

    }

    @GetMapping("/{country}")
    public ModelAndView showTopSongs(ModelAndView modelAndView, @PathVariable("country") String country){
        modelAndView.addObject("country", country);
        if(top500SongsService.findTopSongsByCountry(country) !=null) {
            modelAndView.addObject("listOfTop500Songs", top500SongsService.findTopSongsByCountry(country).getSongs());
            modelAndView.setViewName("topsongs");
            return modelAndView;
        }
        else {

            modelAndView.addObject("listOfTop500Songs", null);
            modelAndView.setViewName("topsongs");
            return modelAndView;
        }
    }
}
