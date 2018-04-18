package com.musixplayer.controller;

import com.musixplayer.model.ArtistData;
import com.musixplayer.model.Song;
import com.musixplayer.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Optional;

@Controller
@RequestMapping("/song")
public class SongController {

    private SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/{mbid}")
    public ModelAndView getSong(ModelAndView modelAndView, @PathVariable("mbid") String mbId) {

        Song song = songService.findSongByMbid(mbId).orElse(songService.fetchSongandAdd(mbId));
        if (song == null) {
            modelAndView.setViewName("error/404");
            return modelAndView;
        }

        // on every click update view count by 1
        int views;
        if(song.getViews()== null)
            views=0;
        else views = song.getViews();
        song = songService.updateSongViews(views + 1, song.getId());

        // for song duration
        int seconds,minutes;
        if(song.getDuration()== null) {
            seconds = 0;
            minutes = 0;
        } else {
            minutes = (song.getDuration())/(1000 * 60);
            seconds = (song.getDuration()/ 1000) % 60;
        }

        modelAndView.addObject("songMbid", mbId);
        modelAndView.addObject("songName", song.getName());
        modelAndView.addObject("songLastfmUrl", song.getLastfmUrl());
        modelAndView.addObject("listOfArtists", song.getArtists());
        modelAndView.addObject("songViews", song.getViews());
        modelAndView.addObject("songCreated", song.getCreated());
        modelAndView.addObject("songMinuteDuration", minutes);
        modelAndView.addObject("songSecondsDuration", seconds);


        if(song.getImageUrl() == null){
            modelAndView.addObject("songImageUrl", "http://www.liquidmoon.co.uk/wp-content/uploads/2014/08/musix-logo-1000x700.jpg");
        }else{
            modelAndView.addObject("songImageUrl", song.getImageUrl());
        }
        if(song.getYoutubeUrl() != null){
            modelAndView.addObject("songYoutubeLink", song.getYoutubeUrl());
        }
        if (song.getDescription() == null) {
            modelAndView.addObject("songDescription", "No description found for this song");
        }else{
            modelAndView.addObject("songDescription", song.getDescription());
        }

        modelAndView.setViewName("song");
        return modelAndView;
    }

}
