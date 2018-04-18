package com.musixplayer.controller;

import com.musixplayer.model.ArtistData;
import com.musixplayer.service.ArtistDataService;
import com.musixplayer.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/artist")
public class ArtistController {

    private ArtistDataService artistDataService;

    @Autowired
    public ArtistController(ArtistDataService artistDataService) {
        this.artistDataService = artistDataService;
    }

    @GetMapping("/{mbid}")
    public ModelAndView getArtist(ModelAndView modelAndView, @PathVariable("mbid") String mbid) {

        ArtistData artist = artistDataService.findByMbid(mbid).orElse(artistDataService.fetchArtistDataandAdd(mbid));
        if (artist == null) {
            modelAndView.setViewName("error/404");
            return modelAndView;
        }

        modelAndView.addObject("artistMbid", mbid);
        modelAndView.addObject("artistName", artist.getName());
        modelAndView.addObject("artistBeginDate", artist.getBegin());
        modelAndView.addObject("artistLastfmUrl", artist.getLastfmUrl());
        modelAndView.addObject("listOfSongs", artist.getSongs());

        if(artist.getImage() == null){
            modelAndView.addObject("artistImageUrl", "http://www.liquidmoon.co.uk/wp-content/uploads/2014/08/musix-logo-1000x700.jpg");
        }else{
            modelAndView.addObject("artistImageUrl", artist.getImage());
        }
        if( artist.getGender() == null){
            modelAndView.addObject("artistGender", "Unknown");
        }else{
            modelAndView.addObject("artistGender", artist.getGender());
        }
        if( artist.getCountry() == null){
            modelAndView.addObject("artistCountry", "Unknown");
        }else{
            modelAndView.addObject("artistCountry", artist.getCountry());
        }
        if (artist.getDescription() == null) {
            modelAndView.addObject("artistDescription", "No description found for this song");
        }else{
            modelAndView.addObject("artistDescription", artist.getDescription());
        }

        modelAndView.setViewName("artist");
        return modelAndView;

    }

    }
